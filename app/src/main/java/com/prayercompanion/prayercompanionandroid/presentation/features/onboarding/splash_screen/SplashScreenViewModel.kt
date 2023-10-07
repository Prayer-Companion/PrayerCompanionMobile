package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.domain.utils.PermissionsManager
import com.prayercompanion.shared.presentation.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.logcat

class SplashScreenViewModel constructor(
    private val permissionsManager: PermissionsManager,
    private val dataStoresRepo: DataStoresRepo,
    isConnectedToInternet: IsConnectedToInternet
) : ViewModel() {

    private val appPreferencesData by lazy { dataStoresRepo.appPreferencesDataStoreData }
    private var startingTimeMillis = System.currentTimeMillis()
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    init {
        viewModelScope.launch {
            val hasInternet = isConnectedToInternet.call()
            logcat { "Has Internet = $hasInternet" }

            if (hasInternet) {
                val user = FirebaseAuth.getInstance().currentUser

                // if the user object is not null means they are signed in
                dataStoresRepo.updateAppPreferencesDataStore {
                    it.copy(isSignedIn = user != null)
                }

                if (user != null) {
                    goToAfterSignIn()
                } else {
                    sendNavigateEvent(Route.SignIn)
                }
            } else {
                val isSignedIn = appPreferencesData.firstOrNull()?.isSignedIn ?: false
                if (isSignedIn) {
                    goToAfterSignIn()
                } else {
                    sendNavigateEvent(Route.SignIn)
                }
            }
        }
    }

    private fun goToAfterSignIn() {
        viewModelScope.launch {
            val hasSkippedNotificationPermission =
                appPreferencesData.firstOrNull()?.hasSkippedNotificationPermission ?: false
            if (permissionsManager.isLocationPermissionGranted && (permissionsManager.isPushNotificationAllowed || hasSkippedNotificationPermission)) {
                sendNavigateEvent(Route.Home)
            } else {
                sendNavigateEvent(Route.PermissionsRequests)
            }
        }
    }

    private fun sendNavigateEvent(route: Route) {
        viewModelScope.launch {
            //minimum splashscreen time is 400ms
            delay(400 - (System.currentTimeMillis() - startingTimeMillis))
            _uiEvents.send(UiEvent.Navigate(route))
        }
    }
}