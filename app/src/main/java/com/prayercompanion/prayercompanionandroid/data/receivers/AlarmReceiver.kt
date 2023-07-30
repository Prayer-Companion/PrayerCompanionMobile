package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.utils.notifications.PrayersNotificationsService
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.domain.usecases.settings.GetIsPauseMediaEnabled
import com.prayercompanion.prayercompanionandroid.domain.utils.MediaController
import com.prayercompanion.prayercompanionandroid.getSerializable
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var prayersNotificationsService: PrayersNotificationsService

    @Inject
    lateinit var getIsPauseMediaEnabled: GetIsPauseMediaEnabled

    @Inject
    lateinit var mediaController: MediaController


    override fun onReceive(context: Context?, intent: Intent?) {
        val item = intent?.getSerializable(
            EXTRA_PRAYER_NOTIFICATION_ITEM,
            PrayerNotificationItem::class.java
        ) ?: kotlin.run {
            Exception("Notification item wasn't received").printStackTraceInDebug()
            return
        }
        CoroutineScope(SupervisorJob()).launch {
            if (getIsPauseMediaEnabled.call()) {
                mediaController.pauseMedia()
            }
        }
        prayersNotificationsService.showNotification(item)
    }

    companion object {
        const val EXTRA_PRAYER_NOTIFICATION_ITEM = "EXTRA_PRAYER_NOTIFICATION_ITEM"
    }
}