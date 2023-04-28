package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import java.lang.Exception
import javax.inject.Inject

class AccountSignIn @Inject constructor(
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