package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.domain.usecases.AccountSignIn
import com.prayercompanion.shared.domain.utils.Task
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.StringResourceReader
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.printStackTraceInDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val accountSignIn: AccountSignIn,
    private val tracker: Tracker,
    private val stringResourceReader: StringResourceReader
) : ScreenModel {

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

            is SignInEvents.OnSignInWithGoogleClicked -> onSignInWithGoogleClicked()
        }
    }

    private fun onSignInWithGoogleResultReceived(
        result: Boolean,
        task: Task<Pair<String?, String?>>
    ) {
        if (result.not()) {
            sendErrorEvent(stringResourceReader.read(StringRes.error_something_went_wrong))
            return
        }

        isLoading = true
        if (task.isSuccessful) {
            val (idToken, accessToken) = task.result ?: return
            if (idToken == null) {
                return
            }
            screenModelScope.launch {
                authenticationRepository.signInWithGoogle(
                    idToken,
                    accessToken,
                    onSuccess = ::onSignInSuccess,
                    onFailure = ::onSignInFail,
                )
            }
        } else {
            isLoading = false
            sendErrorEvent(task.exception?.message)
            task.exception?.printStackTraceInDebug()
        }
    }

    private fun onSignInWithGoogleClicked() {
        tracker.trackButtonClicked(TrackedButtons.GOOGLE_SIGN_IN)
        sendEvent(UiEvent.LaunchSignInWithGoogle)
    }

    private fun onSignInSuccess() {
        isLoading = false
        tracker.trackLogin()
        screenModelScope.launch(Dispatchers.IO) {
            val signInResult = accountSignIn.call()
            signInResult.onSuccess {
                sendEvent(UiEvent.Navigate(Route.PermissionsRequests))
            }.onFailure {
                sendErrorEvent(it.message)
            }
        }
    }

    private fun onSignInFail(exception: Exception) {
        isLoading = false
        sendErrorEvent(exception.message)
    }

    private fun sendErrorEvent(text: String?) {
        if (text.isNullOrBlank()) return
        sendEvent(UiEvent.ShowErrorSnackBarStr(text))
    }

    private fun sendEvent(uiEvent: UiEvent) {
        screenModelScope.launch {
            _uiEventsChannel.send(uiEvent)
        }
    }

}