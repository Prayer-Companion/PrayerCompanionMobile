package com.prayercompanion.shared.domain.utils

expect class PermissionsManager {
    val isLocationPermissionGranted: Boolean

    val isPushNotificationAllowed: Boolean
}