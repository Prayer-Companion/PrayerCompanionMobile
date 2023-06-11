package com.prayercompanion.prayercompanionandroid.presentation.utils

import androidx.activity.result.IntentSenderRequest
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route

sealed class UiEvent {
    data class ShowErrorSnackBar(val errorMessage: UiText) : UiEvent()
    data class Navigate(val route: Route, val args: List<String> = emptyList()) : UiEvent()
    data class RequestPermissions(val permissions: List<String>) : UiEvent()
    data class LaunchIntentSenderRequest(val intentSenderRequest: IntentSenderRequest) : UiEvent()
    object OpenAppSettings : UiEvent()

}