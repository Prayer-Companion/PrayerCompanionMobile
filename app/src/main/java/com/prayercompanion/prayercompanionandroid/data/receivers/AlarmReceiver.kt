package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.utils.PrayersNotificationsService
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.getSerializable
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var prayersNotificationsService: PrayersNotificationsService

    override fun onReceive(context: Context?, intent: Intent?) {
        val item = intent?.getSerializable(
            EXTRA_PRAYER_NOTIFICATION_ITEM,
            PrayerNotificationItem::class.java
        ) ?: kotlin.run {
            Exception("Notification item wasn't received").printStackTraceInDebug()
            return
        }

        prayersNotificationsService.showNotification(item)
    }
    companion object {
        const val EXTRA_PRAYER_NOTIFICATION_ITEM = "EXTRA_PRAYER_NOTIFICATION_ITEM"
    }
}