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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.prayercompanion.shared.presentation.features.main.home_screen.HomeScreen
import com.prayercompanion.shared.presentation.features.main.quran.QuranTab
import com.prayercompanion.shared.presentation.features.main.settings.SettingsScreen

val LocalScaffoldState = compositionLocalOf<ScaffoldState> {
    error("No ScaffoldState provided")
}

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val scaffoldState = rememberScaffoldState()
        CompositionLocalProvider(
            LocalScaffoldState provides scaffoldState
        ) {
            TabNavigator(HomeScreen) {
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
                            TabNavigationItem(HomeScreen)
                            TabNavigationItem(SettingsScreen)
                            TabNavigationItem(QuranTab)
                        }
                    }
                )
            }
        }
    }
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
