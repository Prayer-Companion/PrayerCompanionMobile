package com.prayercompanion.shared.presentation.utils

import androidx.compose.ui.graphics.Color
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.presentation.theme.PrayerStatusAfterHalfTimeColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusJamaahColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusLateColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusMissedColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusNotSetColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusOnTimeColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusQadaaColor

fun getPrayerNameStringRes(prayer: Prayer): StringRes {
    return when (prayer) {
        Prayer.FAJR -> StringRes.fajr
        Prayer.DUHA -> StringRes.sunrise
        Prayer.DHUHR -> StringRes.dhuhr
        Prayer.ASR -> StringRes.asr
        Prayer.MAGHRIB -> StringRes.maghrib
        Prayer.ISHA -> StringRes.isha
    }
}

fun getPrayerStatusNameStringRes(prayerStatus: PrayerStatus, prayer: Prayer): StringRes {
    return when (prayerStatus) {
        PrayerStatus.Jamaah -> StringRes.prayerStatus_in_the_mosque
        PrayerStatus.OnTime -> StringRes.prayerStatus_onTime
        PrayerStatus.AfterHalfTime -> {
            if (prayer == Prayer.ISHA) {
                StringRes.prayerStatus_isha_late1
            } else {
                StringRes.prayerStatus_late1
            }
        }

        PrayerStatus.Late -> {
            if (prayer == Prayer.ISHA) {
                StringRes.prayerStatus_isha_late2
            } else {
                StringRes.prayerStatus_late2
            }
        }

        PrayerStatus.Missed -> StringRes.prayerStatus_missed
        PrayerStatus.Qadaa -> StringRes.prayerStatus_qadaa
        PrayerStatus.None -> StringRes.prayerStatus_none
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