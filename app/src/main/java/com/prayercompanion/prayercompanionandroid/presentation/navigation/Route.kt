package com.prayercompanion.prayercompanionandroid.presentation.navigation

enum class Route(val routeName: String, val bottomNavBar: Boolean = true) {
    SplashScreen(routeName = "SplashScreen", bottomNavBar = false),
    SignIn(routeName = "SignIn", bottomNavBar = false),
    PermissionsRequests(routeName = "PermissionsRequests", bottomNavBar = false),
    Home(routeName = "Home"),
    Qibla(routeName = "Qibla"),
    Quran(routeName = "Quran"),
    Settings(routeName = "Settings"),
    FullQuranSections(routeName = "FullQuranSections/{sections}");

    companion object {
        fun fromStringRoute(str: String?): Route {
            if (str == null)
                return SignIn
            return valueOf(str.split("/")[0])
        }
    }
}