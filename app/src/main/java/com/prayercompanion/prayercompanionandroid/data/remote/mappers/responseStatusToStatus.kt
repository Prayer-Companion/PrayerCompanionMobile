package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus

fun responseStatusToStatus(statusStr: String?): PrayerStatus? {
    return when (statusStr) {
        "jamaah" -> PrayerStatus.Jamaah
        "onTime" -> PrayerStatus.OnTime
        "afterHalfTime" -> PrayerStatus.AfterHalfTime
        "late" -> PrayerStatus.Late
        "missed" -> PrayerStatus.Qadaa
        "qadaa" -> PrayerStatus.Missed
        else -> null
    }
}