package com.prayercompanion.prayercompanionandroid.domain.usecases.settings


import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import javax.inject.Inject

class SetPauseMediaEnabled @Inject constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(enabled: Boolean) {
        dataStoresRepo.appPreferencesDataStore.updateData { it.copy(isPauseMediaPreferencesEnabled = enabled) }
    }
}