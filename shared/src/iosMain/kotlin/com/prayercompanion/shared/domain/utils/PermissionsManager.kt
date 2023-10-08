package com.prayercompanion.shared.domain.utils

actual class PermissionsManager {
    actual val isLocationPermissionGranted: Boolean
        get() {
            TODO()
        }

    actual val isPushNotificationAllowed: Boolean
        get() {
            TODO()
        }


    actual companion object {
        actual val locationPermissions: List<String>
            get() {
                TODO()
            }
        actual val notificationsPermission: String
            get() {
                TODO()
            }
    }
}