package com.prayercompanion.shared.data.repositories

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.presentation.utils.printStackTraceInDebug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual class AuthenticationRepositoryImpl constructor(
    private val googleSignInClient: GoogleSignInClient,
    dataStoresRepo: DataStoresRepo,
): AuthenticationRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val appPreferences by lazy { dataStoresRepo.appPreferencesDataStore }

    actual override fun signInWithGoogle(
        token: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onSignInComplete(task, onSuccess, onFailure)
            }
    }

    actual override fun signInAnonymously(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                onSignInComplete(task, onSuccess, onFailure)
            }
    }

    private fun onSignInComplete(
        task: Task<AuthResult>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (task.isSuccessful) {
            CoroutineScope(Dispatchers.Default).launch {
                appPreferences.updateData {
                    it.copy(isSignedIn = true)
                }
            }
            onSuccess()
        } else {
            signOut()
            task.exception?.let {
                onFailure(it)
                it.printStackTraceInDebug()
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }

}