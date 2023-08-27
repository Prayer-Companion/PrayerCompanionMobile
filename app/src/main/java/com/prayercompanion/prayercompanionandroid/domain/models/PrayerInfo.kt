package com.prayercompanion.prayercompanionandroid.domain.models

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class PrayerInfo(
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    val statusesWithTimeRanges: List<PrayerStatusWithTimeRange>,
    var selectedStatus: PrayerStatus
) : Serializable {
    val date: LocalDate get() = dateTime.toLocalDate()
    val time: LocalTime get() = dateTime.toLocalTime()
    val isStateSelectionEnabled get() = LocalDateTime.now() > dateTime
    val isStateSelectable = prayer != Prayer.DUHA

    companion object {
        val Default = PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
            statusesWithTimeRanges = emptyList(),
            selectedStatus = PrayerStatus.None
        )
    }
}