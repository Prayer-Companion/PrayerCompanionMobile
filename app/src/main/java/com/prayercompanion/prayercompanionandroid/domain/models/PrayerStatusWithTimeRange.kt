package com.prayercompanion.prayercompanionandroid.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PrayerStatusWithTimeRange(
    val prayerStatus: PrayerStatus,
    val range: OpenEndRange<LocalDateTime>?,
    val prayer: Prayer
)