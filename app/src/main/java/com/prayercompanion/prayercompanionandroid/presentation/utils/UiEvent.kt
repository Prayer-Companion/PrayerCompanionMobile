package com.prayercompanion.prayercompanionandroid.presentation.utils

import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route

sealed class UiEvent {
    data class ShowErrorSnackBar(val errorMessage: UiText) : UiEvent()
    data class Navigate(val route: Route) : UiEvent()
    data class RequestPermissions(val permissions: List<String>) : UiEvent()
    object OpenAppSettings : UiEvent()

}