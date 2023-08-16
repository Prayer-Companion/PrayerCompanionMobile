package com.prayercompanion.prayercompanionandroid.domain.models

import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import java.time.LocalDateTime

data class PrayerStatusWithTimeRange(
    val prayerStatus: PrayerStatus,
    val range: OpenEndRange<LocalDateTime>?,
    val text: UiText
)