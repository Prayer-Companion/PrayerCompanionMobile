package com.prayercompanion.prayercompanionandroid.presentation.utils

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import logcat.asLog
import logcat.logcat
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
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    signOut()
                    task.exception?.let {
                        onFailure(it)
                        logcat { it.asLog() }
                    }
                }
            }
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}