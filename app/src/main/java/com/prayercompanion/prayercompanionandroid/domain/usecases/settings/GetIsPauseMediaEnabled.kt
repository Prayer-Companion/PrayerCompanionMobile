package com.prayercompanion.prayercompanionandroid.domain.usecases.settings

import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetIsPauseMediaEnabled @Inject constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(): Boolean {
        return dataStoresRepo
            .appPreferencesDataStore
            .data.firstOrNull()
            ?.isPauseMediaPreferencesEnabled ?: true
    }
}