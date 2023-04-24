package com.prayercompanion.prayercompanionandroid.presentation.features.sign_in

import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText

sealed class UiEvent {
    class ShowErrorSnackBar(val errorMessage: UiText): UiEvent()
}