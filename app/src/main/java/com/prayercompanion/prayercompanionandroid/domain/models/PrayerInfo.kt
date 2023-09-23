package com.prayercompanion.prayercompanionandroid.domain.models

import com.prayercompanion.prayercompanionandroid.domain.extensions.now
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.PrayerStatusWithTimeRange
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class PrayerInfo(
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    val statusesWithTimeRanges: List<PrayerStatusWithTimeRange>,
    var selectedStatus: PrayerStatus
): java.io.Serializable {
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