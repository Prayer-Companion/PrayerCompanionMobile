package com.prayercompanion.shared.presentation.features.onboarding.splash_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.prayercompanion.shared.currentTimeMillis
import com.prayercompanion.shared.data.preferences.AppPreferences
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel constructor(
    private val dataStoresRepo: DataStoresRepo,
    private val authenticationRepository: AuthenticationRepository,
    private val isConnectedToInternet: IsConnectedToInternet
) : ScreenModel {

    private suspend fun appPreferencesData(): AppPreferences? {
        return dataStoresRepo.appPreferencesDataStoreData.firstOrNull()
    }

    private var startingTimeMillis = currentTimeMillis()
    private val _uiEvents = Channel<UiEvent>()

    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: SplashScreenAction) {
        when (action) {
            is SplashScreenAction.OnStart -> onStart(
                action.isLocationPermissionGranted,
                action.isPushNotificationAllowed
            )
        }
    }

    private fun onStart(
        isLocationPermissionGranted: Boolean,
        isPushNotificationAllowed: Boolean
    ) {
        screenModelScope.launch(Dispatchers.Default) {
            val hasInternet = isConnectedToInternet.call()
            log { "Has Internet = $hasInternet" }

            if (hasInternet) {
                val isSignedIn = authenticationRepository.isSignedIn()

                if (isSignedIn) {
                    goToAfterSignIn(
                        isLocationPermissionGranted,
                        isPushNotificationAllowed
                    )
                } else {
                    sendNavigateEvent(Route.SignIn)
                }
            } else {
                val isSignedIn = appPreferencesData()?.isSignedIn ?: false
                if (isSignedIn) {
                    goToAfterSignIn(
                        isLocationPermissionGranted,
                        isPushNotificationAllowed
                    )
                } else {
                    sendNavigateEvent(Route.SignIn)
                }
            }
        }
    }

    private fun goToAfterSignIn(
        isLocationPermissionGranted: Boolean,
        isPushNotificationAllowed: Boolean
    ) {
        screenModelScope.launch(Dispatchers.Default) {
            val skippedNotificationPermission =
                appPreferencesData()?.hasSkippedNotificationPermission ?: false

            if (isLocationPermissionGranted && (isPushNotificationAllowed || skippedNotificationPermission)) {
                sendNavigateEvent(Route.Home)
            } else {
                sendNavigateEvent(Route.PermissionsRequests)
            }
        }
    }

    private fun sendNavigateEvent(route: Route) {
        return // todo remove this once sign in screen is implmeneted
        screenModelScope.launch(Dispatchers.Default) {
            //minimum splashscreen time is 400ms
            delay(400 - (currentTimeMillis() - startingTimeMillis))
            _uiEvents.send(UiEvent.Navigate(route))
        }
    }
}