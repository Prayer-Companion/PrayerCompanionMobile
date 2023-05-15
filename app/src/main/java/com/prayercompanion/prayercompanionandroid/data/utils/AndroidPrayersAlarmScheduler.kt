package com.prayercompanion.prayercompanionandroid.data.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.receivers.AlarmReceiver
import com.prayercompanion.prayercompanionandroid.data.receivers.DailyReceiver
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject

class AndroidPrayersAlarmScheduler @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val getDayPrayers: GetDayPrayers,
    private val prayersNotificationsService: PrayersNotificationsService
) : PrayersAlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleDailyService() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0

        val pendingIntent = PendingIntent.getService(
            context,
            0,
            Intent(context, DailyReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override suspend fun scheduleTodayPrayersNotifications() {
        val now = LocalDateTime.now()
        prayersNotificationsService.showTestNotification("Today's notifications has been scheduled $now")
        getDayPrayers
            .call(now.toLocalDate(), false)
            .getOrElse {
                it.printStackTraceInDebug()
                return
            }
            .prayers.forEach {
                if (it.dateTime >= now) {
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
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_PRAYER_NOTIFICATION_ITEM, item)
        }
        val prayerName = context.getString(item.prayerInfo.prayer.nameId)
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