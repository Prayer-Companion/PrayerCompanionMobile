package com.prayercompanion.prayercompanionandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.HomeScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.sign_in.SignInScreen
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    sealed class BottomNavItem(val route: String, @StringRes val nameId: Int) {
        object Home : BottomNavItem(Route.Home.name, R.string.home_tab)
    }

    private val locationPermissionContract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            // TODO:
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrayerCompanionAndroidTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                var shouldShowBottomNavigationBar by rememberSaveable {
                    mutableStateOf(false)
                }
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val route = Route.valueOf(destination.route ?: Route.SignIn.name)
                    shouldShowBottomNavigationBar = when (route) {
                        Route.SignIn -> false
                        else -> true
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        if (shouldShowBottomNavigationBar) {
                            BottomNavigation(
                                modifier = Modifier.background(
                                    color = MaterialTheme.colors.primary,
                                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                                )
                            ) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination

                                listOf(BottomNavItem.Home).forEach { screen ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                Icons.Filled.Home,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text(stringResource(screen.nameId)) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // re-selecting the same item
                                                launchSingleTop = true
                                                // Restore state when re-selecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Route.SignIn.name,
                    ) {
                        composable(Route.SignIn.name) {
                            SignInScreen(googleSignInClient)
                        }
                        composable(Route.Home.name) {
                            HomeScreen(scaffoldState = scaffoldState)
                        }
                    }
                }
            }
        }

        handleLocationPermission()
    }

    private fun handleLocationPermission() {
        locationPermissionContract.launch(AppLocationManager.permissions)
    }
}