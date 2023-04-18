package com.prayercompanion.prayercompanionandroid.domain.models

import java.time.LocalDateTime

data class PrayerInfo(
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    var status: PrayerStatus
) {

    val date get() = dateTime.toLocalDate()
    val time get() = dateTime.toLocalTime()

    companion object {
        val Default = PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime.now(),
            status = PrayerStatus.NotSet
        )
    }
}