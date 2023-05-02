package com.prayercompanion.prayercompanionandroid.domain.utils

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerNotificationItem
import java.time.LocalDate

interface AlarmScheduler {

    fun schedulePrayerNotification(item: PrayerNotificationItem)

    fun cancel(item: PrayerNotificationItem)
    suspend fun scheduleDayPrayersNotifications(date: LocalDate)
}

