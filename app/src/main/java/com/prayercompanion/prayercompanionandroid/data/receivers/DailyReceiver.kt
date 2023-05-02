package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.utils.AndroidAlarmScheduler
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.getSerializable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import logcat.logcat
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class DailyReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AndroidAlarmScheduler

    @Inject
    lateinit var prayersRepository: PrayersRepository

    @Inject
    lateinit var appLocationManager: AppLocationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        val date = intent?.getSerializable(EXTRA_DATE, LocalDate::class.java) ?: return

        appLocationManager.getLastKnownLocation { location ->
            if (location == null) return@getLastKnownLocation

            CoroutineScope(SupervisorJob()).launch {
                val dayPrayersInfoResult = prayersRepository.getDayPrayers(location, date, false)

                dayPrayersInfoResult.onSuccess { dayPrayers ->
                    dayPrayers.prayers.forEach {
                        logcat { "Notification for ${it.prayer} prayer in ${it.date} was scheduled" }
                        alarmScheduler.schedulePrayerNotification(
                            PrayerNotificationItem(prayerInfo = it, isOngoing = false)
                        )
                    }
//                    prayersRepository.markDayPrayersNotificationsAsScheduled(dayPrayers)
                }
            }

        }
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
    }
}