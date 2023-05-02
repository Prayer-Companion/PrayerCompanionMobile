package com.prayercompanion.prayercompanionandroid.data.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import com.prayercompanion.prayercompanionandroid.data.receivers.AlarmReceiver
import com.prayercompanion.prayercompanionandroid.data.receivers.DailyReceiver
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AlarmScheduler
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override suspend fun scheduleDayPrayersNotifications(date: LocalDate) {
        val location: Location = suspendCoroutine { cont ->
            appLocationManager.getLastKnownLocation { cont.resume(it) }
        } ?: return

        val dayPrayersInfoResult = prayersRepository.getDayPrayers(location, date, false)

        val dayPrayers = dayPrayersInfoResult.getOrNull() ?: return

        dayPrayers.prayers.forEach {

            val notification = PrayerNotificationItem(prayerInfo = it, isOngoing = false)
            schedulePrayerNotification(notification)

            println("Notification for ${it.prayer} prayer in ${it.date} was scheduled")
        }

    }

    @SuppressLint("MissingPermission")
    override fun schedulePrayerNotification(
        item: PrayerNotificationItem
    ) {
        println("${item.prayerInfo.prayer} alarm is set")

        val scheduledTime =
            item.prayerInfo.dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

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

    fun scheduleDailyAlarm() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0

        val pendingIntent = PendingIntent.getService(
            context,
            0,
            Intent(context, DailyReceiver::class.java).also {
                it.putExtra(DailyReceiver.EXTRA_DATE, LocalDate.now())
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun cancel(item: PrayerNotificationItem) {
        val prayerName = context.getString(item.prayerInfo.prayer.nameId)
        val prayerTime = Consts.FullDateTimeFormatter.format(item.prayerInfo.dateTime)

        val notificationId = "$prayerName|$prayerTime".hashCode()

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                notificationId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}