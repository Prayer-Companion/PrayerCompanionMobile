package com.prayercompanion.prayercompanionandroid.domain.models

import java.time.LocalDateTime
import java.time.LocalTime

data class PrayerInfo(
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    var status: PrayerStatus
) {

    val time: LocalTime get() = dateTime.toLocalTime()

    companion object {
        val Default = PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime.now(),
            status = PrayerStatus.NotSet
        )
    }
}