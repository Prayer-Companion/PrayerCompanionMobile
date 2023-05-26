package com.prayercompanion.prayercompanionandroid.data.preferences

import android.content.Context
import androidx.datastore.core.Serializer
import com.prayercompanion.prayercompanionandroid.appPreferencesDataStore
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class DataStoresRepo @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    val appPreferencesDataStore get() =  context.appPreferencesDataStore
}

@Serializable
data class AppPreferences(
    val hasInitiatedFirstDailyNotifications: Boolean
)

object AppPreferencesSerializer : Serializer<AppPreferences> {
    override val defaultValue: AppPreferences
        get() = AppPreferences(
            hasInitiatedFirstDailyNotifications = false
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