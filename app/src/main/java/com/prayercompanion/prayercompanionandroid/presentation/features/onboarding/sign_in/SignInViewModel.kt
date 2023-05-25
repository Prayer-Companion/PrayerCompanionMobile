package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.usecases.AccountSignIn
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.AuthenticationHelper
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import com.prayercompanion.prayercompanionandroid.presentation.utils.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationHelper: AuthenticationHelper,
    private val accountSignIn: AccountSignIn
) : ViewModel() {

    private val _uiEventsChannel = Channel<UiEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()
    var isLoading by mutableStateOf(false)
        private set

    fun onEvent(event: SignInEvents) {
        when (event) {
            is SignInEvents.OnSignInWithGoogleResultReceived -> onSignInWithGoogleResultReceived(
                event.result,
                event.task
            )

            is SignInEvents.OnSignInAnonymously -> onSignInAnonymouslyClicked()
        }
    }

    private fun onSignInWithGoogleResultReceived(result: Boolean, task: Task<GoogleSignInAccount>) {
        if (result.not()) {
            sendErrorEvent(R.string.error_something_went_wrong.toUiText())
            return
        }

        isLoading = true
        if (task.isSuccessful) {
            val token = task.result.idToken ?: return
            authenticationHelper.signInWithGoogle(
                token,
                onSuccess = ::onSignInSuccess,
                onFailure = ::onSignInFail,
            )
        } else {
            isLoading = false
            sendErrorEvent(task.exception?.message.toUiText())
            logcat { task.exception?.asLog() ?: "" }
        }
    }

    private fun onSignInAnonymouslyClicked() {
        authenticationHelper.signInAnonymously(
            onSuccess = ::onSignInSuccess,
            onFailure = ::onSignInFail
        )
    }

    private fun onSignInSuccess() {
        isLoading = false
        viewModelScope.launch(Dispatchers.IO) {
            val signInResult = accountSignIn.call()
            signInResult.onSuccess {
                sendEvent(UiEvent.Navigate(Route.PermissionsRequests))
            }.onFailure {
                sendErrorEvent(it.message.toUiText())
            }
        }
    }

    private fun onSignInFail(exception: Exception) {
        isLoading = false
        sendErrorEvent(exception.message.toUiText())
    }

    private fun sendErrorEvent(text: UiText) {
        sendEvent(UiEvent.ShowErrorSnackBar(text))
    }

    private fun sendEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(uiEvent)
        }
    }

}