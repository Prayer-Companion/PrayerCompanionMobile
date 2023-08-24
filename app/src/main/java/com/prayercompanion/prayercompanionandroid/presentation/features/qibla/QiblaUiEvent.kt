package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

sealed class QiblaUiEvent {
    data object OnStart : QiblaUiEvent()
    data object OnDispose : QiblaUiEvent()
}