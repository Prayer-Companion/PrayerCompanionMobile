package com.prayercompanion.shared.data.repositories

expect class AuthenticationRepositoryImpl {

    suspend fun isSignedIn(): Boolean

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