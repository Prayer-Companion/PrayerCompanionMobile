package com.prayercompanion.prayercompanionandroid.domain.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.LocationServices
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.models.Address
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import logcat.logcat
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppLocationManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val dataStoresRepo: DataStoresRepo,
    private val permissionsManager: PermissionsManager
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? {
        if (permissionsManager.isLocationPermissionGranted.not()) {
            logcat { "Location permission is missing" }
            return dataStoresRepo.appPreferencesDataStore.data.firstOrNull()?.location
        }

        val location = suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        val location = Location(latitude = it.latitude, longitude = it.longitude)
                        continuation.resume(location)
                    } else {
                        continuation.resume(null)
                    }
                }
        }

        if (location == null) {
            return dataStoresRepo.appPreferencesDataStore.data.firstOrNull()?.location
        } else {
            dataStoresRepo.appPreferencesDataStore.updateData {
                it.copy(location = location)
            }
        }

        return location
    }

    @Suppress("DEPRECATION")
    suspend fun getAddress(): Address? {
        val lastSavedAddress = dataStoresRepo.appPreferencesDataStore.data.firstOrNull()?.address

        val location = getLastKnownLocation() ?: return lastSavedAddress
        val gcd = Geocoder(context, Locale.ENGLISH)

        val address: Address? = suspendCoroutine {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gcd.getFromLocation(
                    /* latitude = */ location.latitude,
                    /* longitude = */ location.longitude,
                    /* maxResults = */ 1,
                    /* listener = */ object : Geocoder.GeocodeListener {
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

            } else {
                gcd.getFromLocation(location.latitude, location.longitude, 1)
            }

        }

        if (address != null) {
            dataStoresRepo.appPreferencesDataStore.updateData {
                it.copy(address = address)
            }
        }

        return address ?: lastSavedAddress
    }


}