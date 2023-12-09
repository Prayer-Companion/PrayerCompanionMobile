package com.prayercompanion.shared.presentation.navigation

enum class Route(val routeName: String, val bottomNavBar: Boolean = true) {
    SplashScreen(routeName = "SplashScreen", bottomNavBar = false),
    SignIn(routeName = "SignIn", bottomNavBar = false),
    PermissionsRequests(routeName = "PermissionsRequests", bottomNavBar = false),
    Main(routeName = "Main"),
    Home(routeName = "Home"),
    Qibla(routeName = "Qibla"),
    Quran(routeName = "Quran"),
    Settings(routeName = "Settings"),
    FullQuranSections(routeName = "FullQuranSections");

    companion object {
        fun fromStringRoute(str: String?): Route {
            if (str == null)
                return SignIn
            return valueOf(str.split("/")[0])
        }
    }
}