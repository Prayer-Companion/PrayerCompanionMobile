package com.prayercompanion.prayercompanionandroid.presentation.features.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.prayercompanion.prayercompanionandroid.domain.usecases.AccountSignIn
import com.prayercompanion.prayercompanionandroid.presentation.utils.AuthenticationHelper
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
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
//    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    fun onSignInWithGoogleResultReceived(result: Boolean, task: Task<GoogleSignInAccount>) {
        if (result.not()) {
            sendEvent(
                UiEvent.ShowErrorSnackBar(
                    //todo
                    UiText.DynamicString("Something went wrong")
                )
            )
            return
        }
        if (task.isSuccessful) {
            val token = task.result.idToken ?: return
            authenticationHelper.signInWithGoogle(
                token,
                onSuccess = {
                    viewModelScope.launch(Dispatchers.IO) {
                        accountSignIn.call(token)
                    }
                },
                onFailure = {
                    sendEvent(
                        UiEvent.ShowErrorSnackBar(
                            //todo
                            UiText.DynamicString("Something went wrong")
                        )
                    )
                },
            )
        } else {
            sendEvent(
                UiEvent.ShowErrorSnackBar(
                    //todo
                    UiText.DynamicString("Something went wrong")
                )
            )
            logcat { task.exception?.asLog() ?: "" }
        }
    }

    private fun sendEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(uiEvent)
        }
    }

}