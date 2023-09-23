package com.prayercompanion.prayercompanionandroid.domain.models

import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PrayerStatusWithTimeRange(
    val prayerStatus: PrayerStatus,
    val range: OpenEndRange<LocalDateTime>?,
    val text: UiText
)