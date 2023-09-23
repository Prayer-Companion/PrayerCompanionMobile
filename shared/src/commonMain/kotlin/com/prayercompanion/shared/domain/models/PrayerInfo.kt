package com.prayercompanion.shared.domain.models

import com.prayercompanion.shared.domain.extensions.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class PrayerInfo(
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    val statusesWithTimeRanges: List<PrayerStatusWithTimeRange>,
    var selectedStatus: PrayerStatus
) {
    val date: LocalDate get() = dateTime.date
    val time: LocalTime get() = dateTime.time
    val isStateSelectionEnabled get() = LocalDateTime.now() > dateTime
    val isStateSelectable = prayer != Prayer.DUHA

    companion object {
        val Default = PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime(LocalDate.now(), LocalTime(0, 0)),
            statusesWithTimeRanges = emptyList(),
            selectedStatus = PrayerStatus.None
        )
    }
}