package com.prayercompanion.shared.presentation.features.main.qibla

sealed class QiblaUiEvent {
    data object OnStart : QiblaUiEvent()
    data object OnDispose : QiblaUiEvent()
}