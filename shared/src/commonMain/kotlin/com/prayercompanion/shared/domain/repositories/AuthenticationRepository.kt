package com.prayercompanion.shared.domain.repositories

interface AuthenticationRepository {

    suspend fun isSignedIn(): Boolean

    suspend fun signInWithGoogle(
        token: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun signInAnonymously(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

}