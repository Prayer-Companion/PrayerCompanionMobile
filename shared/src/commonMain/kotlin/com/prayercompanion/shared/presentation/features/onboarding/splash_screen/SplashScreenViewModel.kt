package com.prayercompanion.shared.presentation.features.onboarding.splash_screen

import com.prayercompanion.shared.currentTimeMillis
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.domain.utils.PermissionsManager
import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.log
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel constructor(
    private val permissionsManager: PermissionsManager,
    private val dataStoresRepo: DataStoresRepo,
    authenticationRepository: AuthenticationRepository,
    isConnectedToInternet: IsConnectedToInternet
) : KMMViewModel() {

    private val appPreferencesData by lazy { dataStoresRepo.appPreferencesDataStoreData }
    private var startingTimeMillis = currentTimeMillis()
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    init {
        viewModelScope.coroutineScope.launch {
            val hasInternet = isConnectedToInternet.call()
            log { "Has Internet = $hasInternet" }

            if (hasInternet) {
                val isSignedIn = authenticationRepository.isSignedIn()

                if (isSignedIn) {
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
        viewModelScope.coroutineScope.launch {
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
        viewModelScope.coroutineScope.launch {
            //minimum splashscreen time is 400ms
            delay(400 - (currentTimeMillis() - startingTimeMillis))
            _uiEvents.send(UiEvent.Navigate(route))
        }
    }
}