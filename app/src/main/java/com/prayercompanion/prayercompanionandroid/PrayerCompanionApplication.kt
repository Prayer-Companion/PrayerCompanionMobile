package com.prayercompanion.prayercompanionandroid

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prayercompanion.shared.androidMainModules
import com.prayercompanion.shared.data.local.system.PermissionsManager
import com.prayercompanion.shared.data.local.system.ScheduleDailyPrayersNotificationsWorker
import com.prayercompanion.shared.data.local.system.notification.PrayersNotificationsService
import com.prayercompanion.shared.presentation.appModules
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val androidAppModule = module {
    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
}

class PrayerCompanionApplication : Application(), Configuration.Provider {


    private val permissionsManager by inject<PermissionsManager>()

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .build()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PrayerCompanionApplication)
            modules(
                *appModules().toTypedArray(),
                *androidMainModules().toTypedArray(),
                androidAppModule
            )
        }

        setupNotificationsChannels()
        setupScheduleDailyPrayersWorker()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }

    private fun setupNotificationsChannels() {
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        //Check if the channel was already created
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

        val scheduleDailyPeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ScheduleDailyPrayersNotificationsWorker>(1, TimeUnit.HOURS)
                .build()


        WorkManager.getInstance(this@PrayerCompanionApplication)
            .enqueueUniquePeriodicWork(
                ScheduleDailyPrayersNotificationsWorker::class.simpleName!!,
                ExistingPeriodicWorkPolicy.UPDATE,
                scheduleDailyPeriodicWorkRequest
            )
    }
}