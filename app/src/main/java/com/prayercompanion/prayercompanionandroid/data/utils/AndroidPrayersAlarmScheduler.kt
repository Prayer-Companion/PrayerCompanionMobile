package com.prayercompanion.prayercompanionandroid.data.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.receivers.AlarmReceiver
import com.prayercompanion.prayercompanionandroid.domain.extensions.now
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.presentation.utils.getPrayerNameStringRes
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import com.prayercompanion.shared.domain.models.Prayer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class AndroidPrayersAlarmScheduler constructor(
    private val context: Context,
    private val getDayPrayers: GetDayPrayers
) : PrayersAlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override suspend fun scheduleTodayPrayersNotifications() {
        val now = LocalDateTime.now()
        getDayPrayers
            .call(now.date)
            .getOrElse {
                it.printStackTraceInDebug()
                return
            }
            .prayers.forEach {
                if (it.prayer != Prayer.DUHA && it.dateTime >= now) {
                    val notification = PrayerNotificationItem(prayerInfo = it, isOngoing = false)
                    schedulePrayerNotification(notification)
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun schedulePrayerNotification(
        item: PrayerNotificationItem
    ) {

        val scheduledTime = run {
            item.prayerInfo.dateTime
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_PRAYER_NOTIFICATION_ITEM, item)
        }
        val prayerName = context.getString(getPrayerNameStringRes(item.prayerInfo.prayer))
        val prayerTime = Consts.FullDateTimeFormatter.format(item.prayerInfo.dateTime)

        val notificationId = "$prayerName|$prayerTime".hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            scheduledTime,
            pendingIntent
        )
    }

}