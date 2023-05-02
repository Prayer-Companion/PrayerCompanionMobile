package com.prayercompanion.prayercompanionandroid.data.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.prayercompanion.prayercompanionandroid.MainActivity
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts
import dagger.hilt.android.qualifiers.ApplicationContext
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
            0,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val prayerName = context.getString(item.prayerInfo.prayer.nameId)
        val prayerTime = PresentationConsts.TimeFormatter.format(item.prayerInfo.time)
        val title = context.getString(R.string.prayer_notification_title, prayerName, prayerTime)

        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mosque)
            .setContentTitle(title)
//            .setContentText(item.body)
            .setContentIntent(activityPendingInject)
            .setOngoing(item.isOngoing)
            .build()

        val notificationId = 0 //todo figure out an id

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val CHANNEL_ID = "prayers_notifications_channel"
    }
}