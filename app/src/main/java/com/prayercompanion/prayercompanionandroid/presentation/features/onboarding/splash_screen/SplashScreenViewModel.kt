package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.usecases.IsConnectedToInternet
import com.prayercompanion.prayercompanionandroid.domain.utils.PermissionsManager
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val permissionsManager: PermissionsManager,
    dataStoresRepo: DataStoresRepo,
    isConnectedToInternet: IsConnectedToInternet
) : ViewModel() {

    private val appPreferences by lazy { dataStoresRepo.appPreferencesDataStore }
    private val appPreferencesData by lazy { appPreferences.data }
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
                appPreferences.updateData {
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