package com.prayercompanion.prayercompanionandroid.presentation.navigation

enum class Route(val bottomNavBar: Boolean, val routeName: String) {
    SplashScreen(false, "SplashScreen"),
    SignIn(false, "SignIn"),
    PermissionsRequests(false, "PermissionsRequests"),
    Home(true, "Home"),
    Qibla(true, "Qibla"),
    Quran(true, "Quran"),
    FullQuranSections(true, "FullQuranSections/{sections}");

    companion object {
        fun fromStringRoute(str: String?): Route {
            if (str == null)
                return SignIn
            return valueOf(str.split("/")[0])
        }
    }
}