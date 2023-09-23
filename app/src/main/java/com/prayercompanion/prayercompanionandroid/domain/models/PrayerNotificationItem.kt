package com.prayercompanion.prayercompanionandroid.domain.models

data class PrayerNotificationItem(
    val prayerInfo: PrayerInfo,
    val isOngoing: Boolean
)