package com.prayercompanion.shared.domain.usecases

import com.prayercompanion.shared.data.remote.PrayerCompanionApi

class AccountSignIn constructor(
    private val api: PrayerCompanionApi
) {

    suspend fun call(): Result<Unit> {
        return try {
            api.signIn()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}