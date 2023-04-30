package com.prayercompanion.prayercompanionandroid.presentation.navigation

enum class Route(val bottomNavBar: Boolean) {
    SplashScreen(false),
    SignIn(false),
    PermissionsRequests(false),
    Home(true),
    Qibla(true),
}