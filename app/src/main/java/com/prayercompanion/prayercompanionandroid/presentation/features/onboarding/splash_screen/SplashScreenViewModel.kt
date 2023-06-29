package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.usecases.IsConnectedToInternet
import com.prayercompanion.prayercompanionandroid.domain.usecases.UpdateAuthToken
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
    updateAuthToken: UpdateAuthToken,
    dataStoresRepo: DataStoresRepo,
    isConnectedToInternet: IsConnectedToInternet
) : ViewModel() {

    private val appPreferences by lazy { dataStoresRepo.appPreferencesDataStore.data }
    private var startingTimeMillis = System.currentTimeMillis()
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    init {
        viewModelScope.launch {
            if (isConnectedToInternet.call().also { logcat { "hasInternet = $it" } }.not()) {
                val isSignedIn = appPreferences.firstOrNull()?.isSignedIn ?: false
                if (isSignedIn) {
                    goToAfterSignIn()
                } else {
                    sendNavigateEvent(Route.SignIn)
                }
            } else {
                updateAuthToken.call(
                    forceRefresh = false,
                    onSuccess = ::goToAfterSignIn,
                    onFailure = { // TODO: Handle offline case
                        sendNavigateEvent(Route.SignIn)
                    },
                    onUserNotSignedIn = {
                        sendNavigateEvent(Route.SignIn)
                    },
                )
            }
        }
    }

    private fun goToAfterSignIn() {
        viewModelScope.launch {
            val hasSkippedNotificationPermission =
                appPreferences.firstOrNull()?.hasSkippedNotificationPermission ?: false
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