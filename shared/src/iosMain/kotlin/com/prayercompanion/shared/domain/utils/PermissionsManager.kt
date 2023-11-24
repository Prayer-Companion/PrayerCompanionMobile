package com.prayercompanion.shared.domain.utils

actual class PermissionsManager {
    actual val isLocationPermissionGranted: Boolean
        get() {
            return true
            // TODO("ios")
        }

    actual val isPushNotificationAllowed: Boolean
        get() {
            return true
            // TODO("ios")
        }


    actual companion object {
        actual val locationPermissions: List<String>
            get() {
                return listOf()
                // TODO("ios")
            }
        actual val notificationsPermission: String
            get() {
                return ""
                // TODO("ios")
            }
    }
}