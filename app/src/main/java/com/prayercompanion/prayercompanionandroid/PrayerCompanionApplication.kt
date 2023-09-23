package com.prayercompanion.prayercompanionandroid

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.prayercompanion.prayercompanionandroid.data.di.dataModule
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferences
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferencesSerializer
import com.prayercompanion.prayercompanionandroid.domain.di.domainModule
import com.prayercompanion.prayercompanionandroid.domain.utils.PermissionsManager
import com.prayercompanion.prayercompanionandroid.domain.utils.ScheduleDailyPrayersWorker
import com.prayercompanion.prayercompanionandroid.presentation.di.presentationModule
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayersNotificationsService
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

val Context.appPreferencesDataStore: DataStore<AppPreferences> by dataStore(
    "app_preferences1.json",
    AppPreferencesSerializer
)

class PrayerCompanionApplication : Application(), Configuration.Provider {

    private val permissionsManager: PermissionsManager by inject()

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .build()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PrayerCompanionApplication)
            modules(presentationModule, domainModule, dataModule)
        }

        setupNotificationsChannels()
        setupScheduleDailyPrayersWorker()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
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
        if (permissionsManager.isLocationPermissionGranted.not()) {
            return
        }

        val scheduleDailyPrayersPeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ScheduleDailyPrayersWorker>(1, TimeUnit.HOURS)
                .build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ScheduleDailyPrayersWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.UPDATE,
            scheduleDailyPrayersPeriodicWorkRequest
        )
    }

}