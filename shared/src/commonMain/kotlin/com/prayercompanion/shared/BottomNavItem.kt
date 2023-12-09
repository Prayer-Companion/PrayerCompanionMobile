package com.prayercompanion.shared;

import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.utils.StringRes

enum class BottomNavItem(
    val route: String,
    val stringRes: StringRes,
    val icon: String
) {
    Qibla(Route.Qibla.name, StringRes.qibla_tab, "ic_qibla.xml"),
    Home(Route.Home.name, StringRes.home_tab, "ic_mosque.xml"),
    Quran(Route.Quran.name, StringRes.quran_tab, "ic_quran.xml"),
    Settings(Route.Settings.name, StringRes.settings_tab, "ic_settings.xml");
}