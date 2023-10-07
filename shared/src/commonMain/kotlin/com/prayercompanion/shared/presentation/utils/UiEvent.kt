package com.prayercompanion.shared.presentation.utils

import com.prayercompanion.shared.presentation.navigation.Route

sealed class UiEvent {
    data class ShowErrorSnackBar(val errorMessage: UiText) : UiEvent()
    data class Navigate(val route: Route, val args: List<Int> = emptyList()) : UiEvent()
    data class RequestPermissions(val permissions: List<String>) : UiEvent()
    data class ScrollListToPosition(val position: Int) : UiEvent()
    data class OpenWebUrl(val url: String) : UiEvent()
    data object OpenAppSettings : UiEvent()
    data object ShowRateTheAppPopup : UiEvent()
    data object ShowFeedbackDialog : UiEvent()
    data object LaunchSignInWithGoogle : UiEvent()
}