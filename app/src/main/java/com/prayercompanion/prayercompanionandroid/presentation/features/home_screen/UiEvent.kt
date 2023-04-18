package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText

sealed class UiEvent {
    class ShowErrorSnackBar(val errorMessage: UiText): UiEvent()
}