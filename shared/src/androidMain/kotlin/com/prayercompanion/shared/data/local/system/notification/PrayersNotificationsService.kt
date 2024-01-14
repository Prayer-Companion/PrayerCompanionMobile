package com.prayercompanion.shared.data.local.system.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.core.text.HtmlCompat
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.MainActivity
import com.prayercompanion.shared.data.local.system.receiver.PrayerNotificationActionReceiver
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerNotificationItem
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.presentation.utils.PresentationConsts
import com.prayercompanion.shared.presentation.utils.getPrayerNameStringRes
import com.prayercompanion.shared.presentation.utils.getPrayerStatusNameStringRes
import com.prayercompanion.shared.toJson
import dev.icerock.moko.resources.format
import kotlinx.coroutines.delay

class PrayersNotificationsService constructor(
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(item: PrayerNotificationItem) {
        val notificationId = item.hashCode()

        val activityPendingInject = run {
            val activityIntent = Intent(context, MainActivity::class.java)

            PendingIntent.getActivity(
                context,
                notificationId + "contentIntent".hashCode(),
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        val actionPendingInject = run {

            val actionIntent = Intent(context, PrayerNotificationActionReceiver::class.java).apply {
                putExtra(PrayerNotificationActionReceiver.EXTRA_NOTIFICATION_ID, notificationId)
                putExtra(
                    PrayerNotificationActionReceiver.EXTRA_PRAYER_INFO,
                    item.prayerInfo.toJson()
                )
                putExtra(
                    PrayerNotificationActionReceiver.EXTRA_PRAYER_NOTIFICATION_ACTION,
                    PrayerNotificationAction.Prayed.name
                )
            }

            PendingIntent.getBroadcast(
                context,
                notificationId + "actionIntent".hashCode(),
                actionIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        val prayerName = getPrayerNameStringRes(item.prayerInfo.prayer).getString(context)
        val prayerTime = PresentationConsts.TimeFormatter.format(item.prayerInfo.time)
        val title = Res.strings.prayer_notification_title
            .format(prayerName, prayerTime)
            .toString(context)

        val actionIcon = Icon.createWithResource(context, Res.images.ic_app_logo.drawableResId)
        val actionTitle = Res.strings.notification_action_prayedNow.getString(context)

        val prayedNowAction = Notification.Action.Builder(
            actionIcon,
            actionTitle,
            actionPendingInject
        ).build()

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(Res.images.ic_app_logo.drawableResId)
            .setContentTitle(title)
            .setContentIntent(activityPendingInject)
            .setOngoing(item.isOngoing)
            .setAutoCancel(true)
            .addAction(prayedNowAction)
            .build()


        notificationManager.notify(notificationId, notification)
    }

    suspend fun showNotificationSelectedStatus(
        notificationId: Int,
        prayerInfo: PrayerInfo,
        status: PrayerStatus
    ) {
        val prayerName = getPrayerNameStringRes(prayerInfo.prayer).getString(context)
        val statusName = getPrayerStatusNameStringRes(status, prayerInfo.prayer).getString(context)

        val notificationTitle = Res.strings.notification_action_response_statusUpdate
            .format(prayerName, "<b>$statusName</b>")
            .toString(context)

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(Res.images.ic_app_logo.drawableResId)
            .setContentTitle(
                HtmlCompat.fromHtml(notificationTitle, HtmlCompat.FROM_HTML_MODE_LEGACY)
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
        delay(3000)
        notificationManager.cancel(notificationId)
    }

    fun showTestNotification(title: String, content: String = "") {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingInject = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(Res.images.ic_app_logo.drawableResId)
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