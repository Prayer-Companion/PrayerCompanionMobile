package com.prayercompanion.prayercompanionandroid.presentation.utils

import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route

sealed class UiEvent {
    class ShowErrorSnackBar(val errorMessage: UiText): UiEvent()

    class Navigate(val route: Route): UiEvent()
}