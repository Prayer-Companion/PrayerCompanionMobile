package com.prayercompanion.prayercompanionandroid.domain.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusJamaahColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusLateColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusMissedColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusNotSetColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusOnTimeColor
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerStatusQadaaColor

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
        PrayerStatusJamaahColor
    ),
    OnTime(
        R.drawable.ic_on_time_prayer_status,
        R.string.on_time_prayer_status,
        PrayerStatusOnTimeColor
    ),
    Late(
        R.drawable.ic_late_prayer_status,
        R.string.late_prayer_status,
        PrayerStatusLateColor
    ),
    Qadaa(
        R.drawable.ic_qadaa_prayer_status,
        R.string.qadaa_prayer_status,
        PrayerStatusQadaaColor
    ),
    Missed(
        R.drawable.ic_missed_prayer_status,
        R.string.missed_prayer_status,
        PrayerStatusMissedColor
    ),
    NotSet(
        0,
        0,
        PrayerStatusNotSetColor
    )
}