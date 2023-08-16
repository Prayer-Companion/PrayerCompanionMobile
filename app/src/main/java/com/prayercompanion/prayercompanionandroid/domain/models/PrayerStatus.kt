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
    val color: Color
) {
    Jamaah(PrayerStatusJamaahColor),
    OnTime(PrayerStatusOnTimeColor),
    AfterHalfTime(PrayerStatusAfterHalfTimeColor),
    Late(PrayerStatusLateColor),
    Missed(PrayerStatusMissedColor),
    Qadaa(PrayerStatusQadaaColor),
    None(PrayerStatusNotSetColor);

    @StringRes
    fun getStatusName(prayer: Prayer): Int {
        return if (prayer == Prayer.ISHA) {
            when (this) {
                Jamaah -> R.string.prayerStatus_in_the_mosque
                OnTime -> R.string.prayerStatus_onTime
                AfterHalfTime -> R.string.prayerStatus_isha_late1
                Late -> R.string.prayerStatus_isha_late2
                Missed -> R.string.prayerStatus_missed
                Qadaa -> R.string.prayerStatus_qadaa
                None -> R.string.prayerStatus_none
            }
        } else {
            when (this) {
                Jamaah -> R.string.prayerStatus_in_the_mosque
                OnTime -> R.string.prayerStatus_onTime
                AfterHalfTime -> R.string.prayerStatus_late1
                Late -> R.string.prayerStatus_late2
                Missed -> R.string.prayerStatus_missed
                Qadaa -> R.string.prayerStatus_qadaa
                None -> R.string.prayerStatus_none
            }
        }
    }
}