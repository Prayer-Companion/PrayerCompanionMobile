package com.prayercompanion.shared.presentation.features.onboarding.permissions

import dev.icerock.moko.permissions.Permission

sealed class PermissionsRequestEvent {
    data class OnStart(
        val isLocationPermissionGranted: Boolean,
        val isPushNotificationAllowed: Boolean
    ) : PermissionsRequestEvent()
    data class OnPermissionResult(val permissions: Map<Permission, Boolean>) : PermissionsRequestEvent()

    data object OnSkipNotificationPermission : PermissionsRequestEvent()
    data object OnCTAClicked : PermissionsRequestEvent()
}
