package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

sealed class SignInEvents {
    class OnSignInWithGoogleResultReceived(
        val result: Boolean,
        val task: Task<GoogleSignInAccount>
    ) : SignInEvents()

    object OnSignInAnonymously : SignInEvents()
}