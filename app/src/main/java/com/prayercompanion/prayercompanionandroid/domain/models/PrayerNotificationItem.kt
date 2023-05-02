package com.prayercompanion.prayercompanionandroid.domain.models

import java.io.Serializable

data class PrayerNotificationItem(
    val prayerInfo: PrayerInfo,
    val isOngoing: Boolean
): Serializable