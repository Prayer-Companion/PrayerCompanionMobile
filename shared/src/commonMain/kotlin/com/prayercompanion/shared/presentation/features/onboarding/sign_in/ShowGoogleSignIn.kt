package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import androidx.compose.runtime.Composable
import com.prayercompanion.shared.domain.utils.Task

@Composable
expect fun ShowGoogleSignIn(
    onSignInWithGoogleResultReceived: (Boolean, Task<String>) -> Unit
)