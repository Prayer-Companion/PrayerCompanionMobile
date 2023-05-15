package com.prayercompanion.prayercompanionandroid.domain.utils

interface PrayersAlarmScheduler {

    fun scheduleDailyService()

   suspend fun scheduleTodayPrayersNotifications()
}

