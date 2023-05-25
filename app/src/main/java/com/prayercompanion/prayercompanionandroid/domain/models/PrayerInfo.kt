package com.prayercompanion.prayercompanionandroid.domain.models

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class PrayerInfo(
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    var status: PrayerStatus?
) : Serializable {
    val date: LocalDate get() = dateTime.toLocalDate()
    val time: LocalTime get() = dateTime.toLocalTime()

    companion object {
        val Default = PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
            status = null
        )
    }
}