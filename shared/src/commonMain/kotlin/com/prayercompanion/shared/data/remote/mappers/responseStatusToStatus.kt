package com.prayercompanion.shared.data.remote.mappers

import com.prayercompanion.shared.domain.models.PrayerStatus

fun responseStatusToStatus(statusStr: String?): PrayerStatus {
    return when (statusStr) {
        "jamaah" -> PrayerStatus.Jamaah
        "onTime" -> PrayerStatus.OnTime
        "afterHalfTime" -> PrayerStatus.AfterHalfTime
        "late" -> PrayerStatus.Late
        "missed" -> PrayerStatus.Qadaa
        "qadaa" -> PrayerStatus.Missed
        "none" -> PrayerStatus.None
        else -> PrayerStatus.None
    }
}