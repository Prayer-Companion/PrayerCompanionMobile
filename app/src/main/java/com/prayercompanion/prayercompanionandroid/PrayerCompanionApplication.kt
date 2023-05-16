package com.prayercompanion.prayercompanionandroid

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferences
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferencesSerializer
import com.prayercompanion.prayercompanionandroid.data.utils.PrayersNotificationsService
import dagger.hilt.android.HiltAndroidApp

val Context.appPreferencesDataStore: DataStore<AppPreferences> by dataStore(
    "app_preferences1.json",
    AppPreferencesSerializer
)

@HiltAndroidApp
class PrayerCompanionApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupNotificationsChannels()
    }

    private fun setupNotificationsChannels() {
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        //Check if the channel were already created
        val channels = notificationManager.notificationChannels
        if (channels.any { it.id == (PrayersNotificationsService.CHANNEL_ID) }) {
            return
        }

        val channel = NotificationChannel(
            PrayersNotificationsService.CHANNEL_ID,
            getString(R.string.prayer_reminders_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).also {
            it.description = getString(R.string.prayer_reminders_channel_description)
        }

        notificationManager.createNotificationChannel(channel)
    }
}