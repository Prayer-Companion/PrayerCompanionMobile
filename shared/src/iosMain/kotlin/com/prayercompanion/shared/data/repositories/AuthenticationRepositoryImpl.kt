package com.prayercompanion.shared.data.repositories

actual class AuthenticationRepositoryImpl {

    actual fun signInWithGoogle(
        token: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO()
    }

    actual fun signInAnonymously(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        TODO()
    }
}