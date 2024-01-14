package com.prayercompanion.shared.domain.repositories

interface AuthenticationRepository {

    suspend fun isSignedIn(): Boolean

    suspend fun signInWithGoogle(
        token: String,
        accessToken: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun getIdToken(): String?

    suspend fun signOut()

}