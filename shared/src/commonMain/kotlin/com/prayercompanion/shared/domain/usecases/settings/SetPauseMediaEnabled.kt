package com.prayercompanion.shared.domain.usecases.settings

import com.prayercompanion.shared.data.preferences.DataStoresRepo

class SetPauseMediaEnabled constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(enabled: Boolean) {
        dataStoresRepo.updateAppPreferencesDataStore { it.copy(isPauseMediaPreferencesEnabled = enabled) }
    }
}