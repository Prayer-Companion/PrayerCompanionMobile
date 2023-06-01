package com.prayercompanion.prayercompanionandroid.domain.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionsManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    /**
     * @return true if either [Manifest.permission.ACCESS_FINE_LOCATION] or [Manifest.permission.ACCESS_COARSE_LOCATION] were granted, false otherwise
     * */
    val isLocationPermissionGranted: Boolean
        get() {
            //if either approximate or precise permission were granted then we can proceed
            //both works since approximate is not that far from the real location and can be used
            return locationPermissions
                .map { isPermissionGranted(it) }
                .any { it }
        }

    /**
     * @return true if either OS version is less than 33 or [Manifest.permission.POST_NOTIFICATIONS] is Granted, false otherwise
     * */
    val isPushNotificationAllowed: Boolean
        get() {
            return (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) ||
                isPermissionGranted(notificationsPermission)
        }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            /* context = */ context,
            /* permission = */ permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val locationPermissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        @SuppressLint("InlinedApi")
        const val notificationsPermission = Manifest.permission.POST_NOTIFICATIONS
    }
}