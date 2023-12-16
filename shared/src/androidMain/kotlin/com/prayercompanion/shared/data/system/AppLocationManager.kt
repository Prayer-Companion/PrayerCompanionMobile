package com.prayercompanion.shared.data.system

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address
import com.prayercompanion.shared.domain.utils.ErrorLogger
import com.prayercompanion.shared.domain.utils.PermissionsManager
import com.prayercompanion.shared.presentation.utils.log
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class AppLocationManagerImpl constructor(
    private val context: Context,
    private val dataStoresRepo: DataStoresRepo,
    private val permissionsManager: PermissionsManager,
    private val errorLogger: ErrorLogger
) : AppLocationManager {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    actual override suspend fun getLastKnownLocation(): Location? {
        if (permissionsManager.isLocationPermissionGranted.not()) {
            log { "Location permission is missing" }
            return dataStoresRepo.appPreferencesDataStoreData.firstOrNull()?.location
        }

        val location: Location? = suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        val location = Location(latitude = it.latitude, longitude = it.longitude)
                        log { "Last Location = $location" }
                        continuation.resume(location)
                    } else {
                        continuation.resume(null)
                    }
                }
        }?.also { location ->
            dataStoresRepo.updateAppPreferencesDataStore {
                it.copy(location = location)
            }
        } ?: dataStoresRepo.appPreferencesDataStoreData.firstOrNull()?.location
        return location
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun getAddress(location: Location): Address? {
        val gcd = Geocoder(context, Locale.ENGLISH)
        return suspendCoroutine {
            gcd.getFromLocation(
                location.latitude,
                location.longitude,
                /* maxResults = */ 1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<android.location.Address>) {
                        if (addresses.isNotEmpty()) {
                            val address = addresses.first()
                            val locality = address.locality
                            val countryCode = address.countryCode

                            if (locality != null && countryCode != null) {
                                it.resume(Address(countryCode, locality))
                            } else {
                                it.resume(null)
                            }
                        } else {
                            it.resume(null)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        log { errorMessage.toString() }
                        it.resume(null)
                    }
                }
            )
        }

    }

    @Suppress("DEPRECATION")
    private fun getAddressLegacy(location: Location): Address? {
        val gcd = Geocoder(context, Locale.ENGLISH)

        val addresses = try {
            gcd.getFromLocation(
                location.latitude,
                location.longitude,
                /* maxResults = */ 1
            ) ?: emptyList()
        } catch (e: Exception) {
            errorLogger.logException(e)

            emptyList()
        }

        val address = addresses
            .takeIf { it.isNotEmpty() }
            ?.first() ?: return null

        return Address(address.countryCode, address.locality)
    }

    actual override suspend fun getAddressByLocation(location: Location?): Address? {
        if (location == null)
            return null

        val address: Address? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getAddress(location)
        } else {
            getAddressLegacy(location)
        }

        if (address != null) {
            dataStoresRepo.updateAppPreferencesDataStore {
                it.copy(address = address)
            }
        }

        return address
    }

    @SuppressLint("MissingPermission")
    actual override fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit) {
        if (permissionsManager.isLocationPermissionGranted.not()) {
            log { "Location permission is missing" }
            return
        }

        val mLocationRequest = LocationRequest.Builder(60000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
        val client = LocationServices.getFusedLocationProviderClient(context)

        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                log { "Result = $locationResult" }
                val location = locationResult.lastLocation ?: return
                onLocationRetrieved(location.toAppLocation())
                client.removeLocationUpdates(this)
            }
        }

        client.requestLocationUpdates(mLocationRequest, mLocationCallback, null)

    }

    private fun android.location.Location.toAppLocation(): Location {
        return Location(latitude, longitude)
    }
}