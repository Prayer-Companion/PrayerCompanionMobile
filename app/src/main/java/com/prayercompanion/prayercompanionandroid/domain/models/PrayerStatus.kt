package com.prayercompanion.prayercompanionandroid.domain.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.*

enum class PrayerStatus(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val nameId: Int,
    val color: Color
) {
    Jamaah(
        R.drawable.ic_jamaah_prayer_status,
        R.string.jamaah_prayer_status,
        PrayerStatusJamaah
    ),
    OnTime(
        R.drawable.ic_on_time_prayer_status,
        R.string.on_time_prayer_status,
        PrayerStatusOnTime
    ),
    Late(
        R.drawable.ic_late_prayer_status,
        R.string.late_prayer_status,
        PrayerStatusLate
    ),
    Qadaa(
        R.drawable.ic_qadaa_prayer_status,
        R.string.qadaa_prayer_status,
        PrayerStatusQadaa
    ),
    Missed(
        R.drawable.ic_missed_prayer_status,
        R.string.missed_prayer_status,
        PrayerStatusMissed
    ),
    NotSet(
        0,
        0,
        PrayerStatusNotSet
    )
}