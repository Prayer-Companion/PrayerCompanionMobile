package com.prayercompanion.shared.presentation.features.main.quran

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.prayercompanion.shared.BottomNavItem
import com.prayercompanion.shared.presentation.features.main.quran.quran.QuranScreen
import com.prayercompanion.shared.presentation.utils.createTabOptions

object QuranTab : Tab {
    override val key: ScreenKey
        get() = "QuranTab"

    @Composable
    override fun Content() {
        Navigator(QuranScreen)
    }

    override val options: TabOptions
        @Composable
        get() = createTabOptions(BottomNavItem.Quran)
}