package com.prayercompanion.prayercompanionandroid.domain.usecases.settings

import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import kotlinx.coroutines.flow.firstOrNull

class GetIsPauseMediaEnabled constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(): Boolean {
        return dataStoresRepo
            .appPreferencesDataStore
            .data.firstOrNull()
            ?.isPauseMediaPreferencesEnabled ?: true
    }
}