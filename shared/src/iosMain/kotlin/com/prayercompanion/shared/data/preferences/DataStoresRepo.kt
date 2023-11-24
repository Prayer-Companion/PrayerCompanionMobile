package com.prayercompanion.shared.data.preferences

import com.prayercompanion.shared.fromJson
import com.prayercompanion.shared.toJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.Foundation.NSUserDefaults

actual class DataStoresRepo {

    actual val appPreferencesDataStoreData: Flow<AppPreferences> =
        AppPreferencesDataStore.appPreferencesFlow

    actual suspend fun updateAppPreferencesDataStore(transform: suspend (t: AppPreferences) -> AppPreferences) {
        AppPreferencesDataStore.update(transform)
    }
}

object AppPreferencesDataStore {
    private const val APP_PREFERENCES_KEY = "app_preferences"

    private val defaultAppPreferences = AppPreferences(
        isSignedIn = false,
        location = null,
        address = null,
        hasSkippedNotificationPermission = false,
        deniedLocationPermissionsCount = 0,
        deniedNotificationPermissionsCount = 0,
        hasSetArabicLanguageFirstTime = false,
        isPauseMediaPreferencesEnabled = true,
        hasShownRateTheAppPopup = false
    )
    private val appPreferences: AppPreferences
        get() {
            val ob = NSUserDefaults.standardUserDefaults.objectForKey(APP_PREFERENCES_KEY)?.toString().orEmpty()
            return fromJson<AppPreferences>(ob) ?: defaultAppPreferences
        }

    private val _appPreferencesFlow: MutableStateFlow<AppPreferences> =
        MutableStateFlow(appPreferences)

    val appPreferencesFlow: StateFlow<AppPreferences> = _appPreferencesFlow.asStateFlow()

    suspend fun update(transform: suspend (t: AppPreferences) -> AppPreferences) {
        val newAppPreferences = transform(appPreferences)

        NSUserDefaults.standardUserDefaults.setObject(newAppPreferences.toJson(), forKey = APP_PREFERENCES_KEY)

        _appPreferencesFlow.update {
            newAppPreferences
        }
    }

}