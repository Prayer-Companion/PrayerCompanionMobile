package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
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
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    permissionsManager: PermissionsManager,
    updateAuthToken: UpdateAuthToken,
    dataStoresRepo: DataStoresRepo
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private var startingTimeMillis = System.currentTimeMillis()
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    init {
        updateAuthToken.call(
            forceRefresh = false,
            onSuccess = {
                viewModelScope.launch {
                    val hasSkippedNotificationPermission = dataStoresRepo.appPreferencesDataStore.data.firstOrNull()?.hasSkippedNotificationPermission ?: false
                    if (permissionsManager.isLocationPermissionGranted && (permissionsManager.isPushNotificationAllowed || hasSkippedNotificationPermission)) {
                        sendNavigateEvent(Route.Home)
                    } else {
                        sendNavigateEvent(Route.PermissionsRequests)
                    }
                }
            },
            onFailure = { // TODO: Handle offline case
                sendNavigateEvent(Route.SignIn)
            },
            onUserNotSignedIn = {
                sendNavigateEvent(Route.SignIn)
            },
        )
    }

    private fun sendNavigateEvent(route: Route) {
        viewModelScope.launch {
            //minimum splashscreen time is 400ms
            delay(400 - (System.currentTimeMillis() - startingTimeMillis))
            _uiEvents.send(UiEvent.Navigate(route))
        }
    }
}