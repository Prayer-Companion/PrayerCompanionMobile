package com.prayercompanion.prayercompanionandroid.domain.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusAfterHalfTimeColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusJamaahColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusLateColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusMissedColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusNotSetColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusOnTimeColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusQadaaColor

enum class PrayerStatus(
    @StringRes
    val nameId: Int,
    val color: Color
) {
    Jamaah(
        R.string.jamaah_prayer_status,
        PrayerStatusJamaahColor
    ),
    OnTime(
        R.string.on_time_prayer_status,
        PrayerStatusOnTimeColor
    ),
    AfterHalfTime(
        R.string.after_half_time_prayer_status,
        PrayerStatusAfterHalfTimeColor
    ),
    Late(
        R.string.late_prayer_status,
        PrayerStatusLateColor
    ),
    Qadaa(
        R.string.qadaa_prayer_status,
        PrayerStatusQadaaColor
    ),
    Missed(
        R.string.missed_prayer_status,
        PrayerStatusMissedColor
    )
}

fun PrayerStatus?.getColorOrDefault(): Color {
    return this?.color ?: PrayerStatusNotSetColor
}