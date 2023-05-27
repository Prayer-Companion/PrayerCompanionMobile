package com.prayercompanion.prayercompanionandroid

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferences
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferencesSerializer
import com.prayercompanion.prayercompanionandroid.data.utils.PrayersNotificationsService
import com.prayercompanion.prayercompanionandroid.data.utils.ScheduleDailyPrayersWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val Context.appPreferencesDataStore: DataStore<AppPreferences> by dataStore(
    "app_preferences1.json",
    AppPreferencesSerializer
)

@HiltAndroidApp
class PrayerCompanionApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupNotificationsChannels()
        setupScheduleDailyPrayersWorker()
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

    private fun setupScheduleDailyPrayersWorker() {
        val scheduleDailyPrayersPeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ScheduleDailyPrayersWorker>(15, TimeUnit.MINUTES)
                .build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ScheduleDailyPrayersWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.UPDATE,
            scheduleDailyPrayersPeriodicWorkRequest
        )
    }

}