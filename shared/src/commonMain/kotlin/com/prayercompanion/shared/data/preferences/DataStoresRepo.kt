package com.prayercompanion.shared.data.preferences

import kotlinx.coroutines.flow.Flow

expect class DataStoresRepo {
    val appPreferencesDataStoreData: Flow<AppPreferences>
    suspend fun updateAppPreferencesDataStore(transform: suspend (t: AppPreferences) -> AppPreferences)
}