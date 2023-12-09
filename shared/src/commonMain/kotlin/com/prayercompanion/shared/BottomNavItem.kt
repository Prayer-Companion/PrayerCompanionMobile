package com.prayercompanion.shared;

import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.presentation.navigation.Route
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

enum class BottomNavItem(
    val route: String,
    val stringRes: StringResource,
    val icon: ImageResource
) {
    Qibla(Route.Qibla.name, Res.strings.qibla_tab, Res.images.ic_qibla),
    Home(Route.Home.name, Res.strings.home_tab, Res.images.ic_mosque),
    Quran(Route.Quran.name, Res.strings.quran_tab, Res.images.ic_quran),
    Settings(Route.Settings.name, Res.strings.settings_tab, Res.images.ic_settings);
}