package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import com.prayercompanion.shared.domain.utils.Task

sealed class SignInEvents {
    class OnSignInWithGoogleResultReceived(
        val result: Boolean,
        val task: Task<Pair<String?,String?>>
    ) : SignInEvents()

    data object OnSignInWithGoogleClicked : SignInEvents()
}