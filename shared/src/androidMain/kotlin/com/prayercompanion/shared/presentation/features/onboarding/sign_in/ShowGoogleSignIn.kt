package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import com.prayercompanion.shared.domain.utils.Task

typealias googleSignInResultCallback = (Boolean, Task<Pair<String?, String?>>) -> Unit

object GoogleSignInSetup {

    private var signInWithGoogle: (() -> Unit)? = null
    private var onSignInWithGoogleResultReceived: googleSignInResultCallback = { _, _ -> }

    /** called from androidApp/MainActivity to setup the callback to call google sign-in */
    fun setup(signInWithGoogle: () -> Unit) {
        this.signInWithGoogle = signInWithGoogle
    }

    /** called from androidMain to initiate sign in*/
    fun signIn(onSignInWithGoogleResultReceived: googleSignInResultCallback) {
        this.onSignInWithGoogleResultReceived = onSignInWithGoogleResultReceived
        signInWithGoogle?.invoke()
    }

    /** called from androidApp/MainActivity to share the activity result of sign-in*/
    fun onResult(success: Boolean, task: Task<Pair<String?, String?>>) {
        onSignInWithGoogleResultReceived(success, task)
    }
}

actual fun showGoogleSignIn(
    onSignInWithGoogleResultReceived: (Boolean, Task<Pair<String?, String?>>) -> Unit
) {
    GoogleSignInSetup.signIn(onSignInWithGoogleResultReceived)
}