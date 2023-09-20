package com.prayercompanion.prayercompanionandroid.data.preferences

import android.content.Context
import androidx.datastore.core.Serializer
import com.prayercompanion.prayercompanionandroid.appPreferencesDataStore
import com.prayercompanion.prayercompanionandroid.domain.models.Address
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class DataStoresRepo constructor(
    private val context: Context
) {
    val appPreferencesDataStore get() =  context.appPreferencesDataStore
}

@Serializable
data class AppPreferences(
    val isSignedIn: Boolean,
    val location: Location?,
    val address: Address?,
    val hasSkippedNotificationPermission: Boolean,
    val deniedLocationPermissionsCount: Int,
    val deniedNotificationPermissionsCount: Int,
    /** The default app language should be arabic, so we set it to Arabic the first time only */
    val hasSetArabicLanguageFirstTime: Boolean,
    val isPauseMediaPreferencesEnabled: Boolean,
    val hasShownRateTheAppPopup: Boolean
)

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