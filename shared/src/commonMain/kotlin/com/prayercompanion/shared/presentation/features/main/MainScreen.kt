package com.prayercompanion.shared.presentation.features.main


import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.prayercompanion.shared.BottomNavItem
import com.prayercompanion.shared.presentation.features.main.home_screen.HomeScreen
import com.prayercompanion.shared.presentation.features.main.quran.quran.QuranScreen
import com.prayercompanion.shared.presentation.features.main.settings.SettingsScreen
import com.prayercompanion.shared.presentation.utils.createTabOptions

object MainScreen : Screen {

    @Composable
    override fun Content() {

        val scaffoldState = rememberScaffoldState()
        val homeScreen = HomeScreen(scaffoldState)

        TabNavigator(homeScreen) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                snackbarHost = {
                    SnackbarHost(it) { data ->
                        Snackbar(
                            snackbarData = data,
                            backgroundColor = Color.LightGray,
                        )
                    }
                },
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    BottomNavigation {
                        TabNavigationItem(homeScreen)
                        TabNavigationItem(SettingsScreen)
                        TabNavigationItem(QuranTab(scaffoldState))
                    }
                }
            )
        }
    }
}

class QuranTab(private val scaffoldState: ScaffoldState) : Tab {
    override val key: ScreenKey
        get() = "QuranTab"

    @Composable
    override fun Content() {
        Navigator(QuranScreen(scaffoldState))
    }

    override val options: TabOptions
        @Composable
        get() = createTabOptions(BottomNavItem.Quran)
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}
