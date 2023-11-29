package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import cocoapods.GoogleSignIn.GIDSignIn
import com.prayercompanion.shared.domain.utils.Task
import com.prayercompanion.shared.presentation.uiViewController

actual fun showGoogleSignIn(
    onSignInWithGoogleResultReceived: (Boolean, Task<Pair<String?, String?>>) -> Unit
) {
    GIDSignIn.sharedInstance.signOut() //todo remove
    GIDSignIn.sharedInstance.signInWithPresentingViewController(uiViewController) { result, error ->
        val token = result?.user?.idToken?.tokenString
        val accessToken = result?.user?.refreshToken?.tokenString
        onSignInWithGoogleResultReceived(
            token != null, Task(
                isSuccessful = token != null,
                result = token to accessToken,
                exception = Exception(error?.description ?: "Unknown error")
            )
        )
    }
}