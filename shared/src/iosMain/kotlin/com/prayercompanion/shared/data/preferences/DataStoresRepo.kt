package com.prayercompanion.shared.data.preferences

import kotlinx.coroutines.flow.Flow

actual class DataStoresRepo {
    actual val appPreferencesDataStoreData: Flow<AppPreferences> = TODO()
    actual suspend fun updateAppPreferencesDataStore(transform: suspend (t: AppPreferences) -> AppPreferences) {
        TODO()
    }
}