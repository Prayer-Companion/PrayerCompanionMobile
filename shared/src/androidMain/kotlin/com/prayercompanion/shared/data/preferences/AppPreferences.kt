package com.prayercompanion.shared.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.prayercompanion.shared.presentation.utils.printStackTraceInDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

val Context.appPreferencesDataStore: DataStore<AppPreferences> by dataStore(
    "app_preferences1.json",
    AppPreferencesSerializer
)

actual class DataStoresRepo constructor(
    private val context: Context
) {
    actual val appPreferencesDataStoreData: Flow<AppPreferences> get() = context.appPreferencesDataStore.data
    actual suspend fun updateAppPreferencesDataStore(transform: suspend (t: AppPreferences) -> AppPreferences) {
        context.appPreferencesDataStore.updateData(transform)
    }
}

object AppPreferencesSerializer : Serializer<AppPreferences> {
    override val defaultValue: AppPreferences
        get() = AppPreferences(
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

    override suspend fun readFrom(input: InputStream): AppPreferences {
        return try {
            Json.decodeFromString(
                deserializer = AppPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTraceInDebug()
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: AppPreferences,
        output: OutputStream
    ) = withContext(Dispatchers.IO) {
        output.write(
            Json.encodeToString(
                serializer = AppPreferences.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}