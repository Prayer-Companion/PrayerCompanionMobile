package com.prayercompanion.shared.data.local.system

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionsManager constructor(
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
    }
}