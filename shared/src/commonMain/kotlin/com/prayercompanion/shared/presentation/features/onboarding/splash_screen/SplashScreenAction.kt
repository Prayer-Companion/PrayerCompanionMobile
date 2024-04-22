package com.prayercompanion.shared.presentation.features.onboarding.splash_screen

sealed class SplashScreenAction {
    data class OnStart(
        val isLocationPermissionGranted: Boolean,
        val isPushNotificationAllowed: Boolean
    ): SplashScreenAction()
}