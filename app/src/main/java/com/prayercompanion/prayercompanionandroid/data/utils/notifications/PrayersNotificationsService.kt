package com.prayercompanion.prayercompanionandroid.data.utils.notifications

import android.app.Notification
import android.app.Notification.Action
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import com.prayercompanion.prayercompanionandroid.MainActivity
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.data.receivers.PrayerNotificationActionReceiver
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

        val actionIntent = Intent(context, PrayerNotificationActionReceiver::class.java).apply {
            putExtra(PrayerNotificationActionReceiver.EXTRA_PRAYER_INFO, item.prayerInfo)
            putExtra(PrayerNotificationActionReceiver.EXTRA_PRAYER_NOTIFICATION_ACTION, PrayerNotificationAction.Prayed)
        }

        val actionPendingInject = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        //todo notification: add translation
        val action = Action.Builder(Icon.createWithResource(context, R.drawable.ic_app_logo), "click here", actionPendingInject).build()

        val prayerName = context.getString(item.prayerInfo.prayer.nameId)
        val title = context.getString(R.string.prayer_notification_title, prayerName)

        val notification = Notification
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentIntent(activityPendingInject)
            .setOngoing(item.isOngoing)
            .setAutoCancel(true)
            .addAction(action)
            .build()

        val notificationId = item.hashCode()

        logcat { "Notified: $item" }
        notificationManager.notify(notificationId, notification)
    }

    fun showTestNotification(title: String, content: String = "") {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingInject = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentIntent(activityPendingInject)
            .setContentText(content)
            .setAutoCancel(true)
            .build()

        val notificationId = (title + content).hashCode()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val REQUEST_CODE = 111
        const val CHANNEL_ID = "prayers_notifications_channel"
    }
}