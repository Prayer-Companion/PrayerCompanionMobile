package com.prayercompanion.prayercompanionandroid.domain.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import logcat.logcat
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppLocationManager @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val areAllPermissionsGranted: Boolean
        get() {
            return permissions
                .map {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }.all { it }
        }

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? {
        if (areAllPermissionsGranted.not()) {
            logcat { "Location permission is missing" }
            return null
        }

        return suspendCoroutine {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    it.resume(location)
                }
        }
    }

    companion object {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}