package com.prayercompanion.shared.domain.usecases.settings

import com.prayercompanion.shared.data.preferences.DataStoresRepo
import kotlinx.coroutines.flow.firstOrNull

class GetIsPauseMediaEnabled constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(): Boolean {
        return dataStoresRepo
            .appPreferencesDataStoreData
            .firstOrNull()
            ?.isPauseMediaPreferencesEnabled ?: true
    }
}