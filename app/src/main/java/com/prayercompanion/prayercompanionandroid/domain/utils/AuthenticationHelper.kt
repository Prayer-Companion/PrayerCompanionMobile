package com.prayercompanion.prayercompanionandroid.domain.utils

interface AuthenticationHelper {

    fun signInWithGoogle(
        token: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun signInAnonymously(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}