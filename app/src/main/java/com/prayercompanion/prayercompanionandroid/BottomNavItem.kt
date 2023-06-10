package com.prayercompanion.prayercompanionandroid

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route

enum class BottomNavItem(
    val route: String,
    @StringRes val nameId: Int,
    @DrawableRes val icon: Int
) {
    Qibla(Route.Qibla.name, R.string.qibla_tab, R.drawable.ic_qibla),
    Home(Route.Home.name, R.string.home_tab, R.drawable.ic_mosque),
    Quran(Route.Quran.name, R.string.quran_tab, R.drawable.ic_quran);

    companion object {

        fun getOrdered(): List<BottomNavItem> {
            return listOf(
                Quran,
                Home,
                Qibla
            )
        }
    }
}
