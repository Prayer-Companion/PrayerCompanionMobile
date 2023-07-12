package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.presentation.utils.AuthenticationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateAuthToken @Inject constructor(
    private val authenticationHelper: AuthenticationHelper,
    dataStoresRepo: DataStoresRepo,
) {

    private val auth = FirebaseAuth.getInstance()
    private val appPreferences by lazy { dataStoresRepo.appPreferencesDataStore }

    fun call(
        forceRefresh: Boolean,
        onSuccess: () -> Unit = {},
        onFailure: (exception: Exception) -> Unit = {},
        onUserNotSignedIn: () -> Unit = {}
    ) {
        fun updateIsSignedIn(isSignedIn: Boolean) {
            CoroutineScope(Dispatchers.Default).launch {
                appPreferences.updateData {
                    it.copy(isSignedIn = isSignedIn)
                }
            }
        }
        val user = auth.currentUser ?: kotlin.run {
            updateIsSignedIn(false)
            onUserNotSignedIn()
            return
        }

        updateIsSignedIn(true)
        user.getIdToken(forceRefresh)
            .addOnSuccessListener {
                Consts.userTokenUpdateTime = LocalDateTime.now()
                Consts.userToken = it.token
                onSuccess()
            }.addOnFailureListener { exception ->
                authenticationHelper.signOut()
                onFailure(exception)
            }
    }
}