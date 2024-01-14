package com.prayercompanion.shared.presentation.utils

import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.presentation.theme.PrayerStatusAfterHalfTimeColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusJamaahColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusLateColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusMissedColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusNotSetColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusOnTimeColor
import com.prayercompanion.shared.presentation.theme.PrayerStatusQadaaColor
import dev.icerock.moko.resources.StringResource

fun getPrayerNameStringRes(prayer: Prayer): StringResource {
    return when (prayer) {
        Prayer.FAJR -> Res.strings.fajr
        Prayer.DUHA -> Res.strings.sunrise
        Prayer.DHUHR -> Res.strings.dhuhr
        Prayer.ASR -> Res.strings.asr
        Prayer.MAGHRIB -> Res.strings.maghrib
        Prayer.ISHA -> Res.strings.isha
    }
}

fun getPrayerStatusNameStringRes(prayerStatus: PrayerStatus, prayer: Prayer): StringResource {
    return when (prayerStatus) {
        PrayerStatus.Jamaah -> Res.strings.prayerStatus_in_the_mosque
        PrayerStatus.OnTime -> Res.strings.prayerStatus_onTime
        PrayerStatus.AfterHalfTime -> {
            if (prayer == Prayer.ISHA) {
                Res.strings.prayerStatus_isha_late1
            } else {
                Res.strings.prayerStatus_late1
            }
        }

        PrayerStatus.Late -> {
            if (prayer == Prayer.ISHA) {
                Res.strings.prayerStatus_isha_late2
            } else {
                Res.strings.prayerStatus_late2
            }
        }

        PrayerStatus.Missed -> Res.strings.prayerStatus_missed
        PrayerStatus.Qadaa -> Res.strings.prayerStatus_qadaa
        PrayerStatus.None -> Res.strings.prayerStatus_none
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