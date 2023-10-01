package com.prayercompanion.shared.domain.repositories

interface AuthenticationRepository {

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