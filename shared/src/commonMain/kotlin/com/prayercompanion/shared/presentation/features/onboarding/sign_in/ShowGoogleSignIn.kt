package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import com.prayercompanion.shared.domain.utils.Task

expect fun showGoogleSignIn(
    onSignInWithGoogleResultReceived: (Boolean, Task<Pair<String?,String?>>) -> Unit
)