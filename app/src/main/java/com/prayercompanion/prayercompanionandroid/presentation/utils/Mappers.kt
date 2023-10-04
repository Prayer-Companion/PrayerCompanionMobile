package com.prayercompanion.prayercompanionandroid.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.presentation.theme.PrayerStatusAfterHalfTimeColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusJamaahColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusLateColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusMissedColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusNotSetColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusOnTimeColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusQadaaColor

@StringRes
fun getPrayerNameStringRes(prayer: Prayer): Int {
    return when (prayer) {
        Prayer.FAJR -> R.string.fajr
        Prayer.DUHA -> R.string.sunrise
        Prayer.DHUHR -> R.string.dhuhr
        Prayer.ASR -> R.string.asr
        Prayer.MAGHRIB -> R.string.maghrib
        Prayer.ISHA -> R.string.isha
    }
}

@StringRes
fun getPrayerStatusNameStringRes(prayerStatus: PrayerStatus, prayer: Prayer): Int {
    return when (prayerStatus) {
        PrayerStatus.Jamaah -> R.string.prayerStatus_in_the_mosque
        PrayerStatus.OnTime -> R.string.prayerStatus_onTime
        PrayerStatus.AfterHalfTime -> {
            if (prayer == Prayer.ISHA) {
                R.string.prayerStatus_isha_late1
            } else {
                R.string.prayerStatus_late1
            }
        }

        PrayerStatus.Late -> {
            if (prayer == Prayer.ISHA) {
                R.string.prayerStatus_isha_late2
            } else {
                R.string.prayerStatus_late2
            }
        }

        PrayerStatus.Missed -> R.string.prayerStatus_missed
        PrayerStatus.Qadaa -> R.string.prayerStatus_qadaa
        PrayerStatus.None -> R.string.prayerStatus_none
    }
}

fun getPrayerStatusCorrespondingColor(prayerStatus: PrayerStatus): Color {
    return when(prayerStatus) {
        PrayerStatus.Jamaah -> PrayerStatusJamaahColor
        PrayerStatus.OnTime -> PrayerStatusOnTimeColor
        PrayerStatus.AfterHalfTime -> PrayerStatusAfterHalfTimeColor
        PrayerStatus.Late -> PrayerStatusLateColor
        PrayerStatus.Missed -> PrayerStatusMissedColor
        PrayerStatus.Qadaa -> PrayerStatusQadaaColor
        PrayerStatus.None -> PrayerStatusNotSetColor
    }
}