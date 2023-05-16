package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.AuthenticationHelper
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    appLocationManager: AppLocationManager,
    authenticationHelper: AuthenticationHelper
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private var startingTimeMillis = System.currentTimeMillis()
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    init {
        val user = auth.currentUser
        if (user == null) {
            sendNavigateEvent(Route.SignIn)
        } else {
            user.getIdToken(false)
                .addOnSuccessListener {
                    Consts.userToken = it.token
                    if (appLocationManager.areAllPermissionsGranted) { //todo this sometimes doesn't work for some reason
                        sendNavigateEvent(Route.Home)
                    } else {
                        sendNavigateEvent(Route.PermissionsRequests)
                    }
                }
                .addOnFailureListener {
                    // TODO: Handle offline case
                    authenticationHelper.signOut()
                    sendNavigateEvent(Route.SignIn)
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