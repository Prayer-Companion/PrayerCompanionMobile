package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import javax.inject.Inject

class AccountSignIn @Inject constructor(
    private val api: PrayerCompanionApi
) {

    suspend fun call() {
        kotlin.runCatching {
            api.signIn()
        }
    }
}