package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

sealed class QiblaUiEvent {
    object OnStart : QiblaUiEvent()
    object OnDispose : QiblaUiEvent()
}