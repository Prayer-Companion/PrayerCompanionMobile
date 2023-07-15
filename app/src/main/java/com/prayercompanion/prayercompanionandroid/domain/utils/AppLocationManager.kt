package com.prayercompanion.prayercompanionandroid.domain.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.models.Address
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import com.prayercompanion.prayercompanionandroid.domain.models.toAppLocation
import com.skydoves.whatif.whatIfNotNull
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import logcat.logcat
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface AppLocationManager {
    suspend fun getLastKnownLocation(): Location?
    suspend fun getAddressByLocation(location: Location?): Address?
    fun checkLocationService(): Task<LocationSettingsResponse>
    fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit)
}

class AppLocationManagerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val dataStoresRepo: DataStoresRepo,
    private val permissionsManager: PermissionsManager
) : AppLocationManager {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): Location? {
        if (permissionsManager.isLocationPermissionGranted.not()) {
            logcat { "Location permission is missing" }
            return dataStoresRepo.appPreferencesDataStore.data.firstOrNull()?.location
        }

        val location: Location? = suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        val location = Location(latitude = it.latitude, longitude = it.longitude)
                        logcat { "Last Location = $location" }
                        continuation.resume(location)
                    } else {
                        continuation.resume(null)
                    }
                }
        }.whatIfNotNull { location ->
            dataStoresRepo.appPreferencesDataStore.updateData {
                it.copy(location = location)
            }
        } ?: dataStoresRepo.appPreferencesDataStore.data.firstOrNull()?.location
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
                            it.resume(Address(address.countryCode, address.locality))
                        } else {
                            it.resume(null)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        logcat { errorMessage.toString() }
                        it.resume(null)
                    }
                }
            )
        }

    }

    @Suppress("DEPRECATION")
    private fun getAddressLegacy(location: Location): Address? {
        val gcd = Geocoder(context, Locale.ENGLISH)

        val addresses = gcd.getFromLocation(
            location.latitude,
            location.longitude,
            /* maxResults = */ 1
        ) ?: emptyList()

        val address = addresses
            .takeIf { it.isNotEmpty() }
            ?.first() ?: return null

        return Address(address.countryCode, address.locality)
    }

    override suspend fun getAddressByLocation(location: Location?): Address? {
        if (location == null)
            return null

        val address: Address? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getAddress(location)
        } else {
            getAddressLegacy(location)
        }

        if (address != null) {
            dataStoresRepo.appPreferencesDataStore.updateData {
                it.copy(address = address)
            }
        }

        return address
    }

    override fun checkLocationService(): Task<LocationSettingsResponse> {
        val intervalForLocationUpdateInMillis = 10000L

        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalForLocationUpdateInMillis)
            .build()

        val locationSettingsRequest = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(context)

        return client.checkLocationSettings(locationSettingsRequest)
    }

    @SuppressLint("MissingPermission")
    override fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit) {
        if (permissionsManager.isLocationPermissionGranted.not()) {
            logcat { "Location permission is missing" }
            return
        }

        val mLocationRequest = LocationRequest.Builder(60000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
        val client = LocationServices.getFusedLocationProviderClient(context)

        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                logcat { "Result = $locationResult" }
                val location = locationResult.lastLocation ?: return
                onLocationRetrieved(location.toAppLocation())
                client.removeLocationUpdates(this)
            }
        }

        client.requestLocationUpdates(mLocationRequest, mLocationCallback, null)

    }
}