package com.prayercompanion.shared.data.repositories

import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.presentation.utils.printStackTraceInDebug
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl constructor(
    private val dataStoresRepo: DataStoresRepo,
) : AuthenticationRepository {

    private val auth = Firebase.auth

    init {
        auth.currentUser?.uid?.let {
            Firebase.crashlytics.setUserId(it)
        }
    }

    override suspend fun isSignedIn(): Boolean {
        val user = auth.currentUser

        // if the user object is not null means they are signed in
        dataStoresRepo.updateAppPreferencesDataStore {
            it.copy(isSignedIn = user != null)
        }
        return user != null
    }

    override suspend fun signInWithGoogle(
        token: String,
        accessToken: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        signOut()
        val credential = GoogleAuthProvider.credential(token, accessToken)
        try {
            val result = auth.signInWithCredential(credential)

            CoroutineScope(Dispatchers.Default).launch {
                dataStoresRepo.updateAppPreferencesDataStore {
                    it.copy(isSignedIn = true)
                }
            }

            result.user?.uid?.let {
                Firebase.crashlytics.setUserId(it)
            }
            onSuccess()
        } catch (e: Exception) {
            onSignInFail(e, onFailure)
            e.printStackTraceInDebug()
        }
    }

    override suspend fun getIdToken(): String? {
        return auth.currentUser?.getIdToken(false)
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    private suspend fun onSignInFail(
        exception: Exception,
        onFailure: (Exception) -> Unit
    ) {
        signOut()
        onFailure(exception)
        exception.printStackTraceInDebug()
    }
}