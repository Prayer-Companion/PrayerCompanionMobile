package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions

sealed class PermissionsRequestEvent {
    data class OnPermissionResult(val permissions: Map<String, Boolean>) : PermissionsRequestEvent()
    object OnSkipNotificationPermission : PermissionsRequestEvent()
    object OnCTAClicked : PermissionsRequestEvent()
}
