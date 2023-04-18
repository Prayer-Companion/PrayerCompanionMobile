package com.prayercompanion.prayercompanionandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    sealed class BottomNavItem(val route: String, @StringRes val nameId: Int) {
        object Home : BottomNavItem(Route.HOME, R.string.home_tab)
    }


    private val locationPermissionContract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { locationsPermissions ->
            // TODO:
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrayerCompanionAndroidTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState,
                ) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Route.HOME,
                    ) {

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