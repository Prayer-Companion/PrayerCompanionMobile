package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAppLanguage @Inject constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(): AppLanguage? {
        return dataStoresRepo.appPreferencesDataStore.data.first().appLanguage
    }
}