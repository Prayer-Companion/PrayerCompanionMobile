package com.prayercompanion.prayercompanionandroid.presentation.utils

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import javax.inject.Inject

class AuthenticationHelper @Inject constructor(
    private val googleSignInClient: GoogleSignInClient
) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithGoogle(
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

    fun signInAnonymously(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                onSignInComplete(task, onSuccess, onFailure)
            }
    }

    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
        Consts.userToken = null
    }

    private fun onSignInComplete(
        task: Task<AuthResult>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (task.isSuccessful) {
            task.result.user?.getIdToken(false)
                ?.addOnSuccessListener {
                    Consts.userToken = it.token
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

}