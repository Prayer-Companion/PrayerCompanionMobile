package com.prayercompanion.shared.domain.models

data class PrayerNotificationItem(
    val prayerInfo: PrayerInfo,
    val isOngoing: Boolean
)