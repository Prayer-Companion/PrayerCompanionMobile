package com.prayercompanion.prayercompanionandroid.data.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.prayercompanion.prayercompanionandroid.MainActivity
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import dagger.hilt.android.qualifiers.ApplicationContext
import logcat.logcat
import javax.inject.Inject

class PrayersNotificationsService @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(item: PrayerNotificationItem) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingInject = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val prayerName = context.getString(item.prayerInfo.prayer.nameId)
        val title = context.getString(R.string.prayer_notification_title, prayerName)

        val notification = Notification
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentIntent(activityPendingInject)
            .setOngoing(item.isOngoing)
            .build()

        val notificationId = item.hashCode()

        logcat { "Notified: $item" }
        notificationManager.notify(notificationId, notification)
    }

    fun showTestNotification(title: String, content: String = "") {
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentText(content)
            .build()

        val notificationId = (title + content).hashCode()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val REQUEST_CODE = 111
        const val CHANNEL_ID = "prayers_notifications_channel"
    }
}