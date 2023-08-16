package com.prayercompanion.prayercompanionandroid.domain.models

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class DayPrayersInfo constructor(
    val prayers: List<PrayerInfo>
) {

    init {
        require(prayers.map { it.prayer } == Prayer.values().toList()) {
            "DayPrayersInfo created on wrong prayers ${prayers.map { it.prayer }}"
        }
    }

    fun get(prayer: Prayer): PrayerInfo {
        return prayers.find { it.prayer == prayer } ?: throw Exception("prayer not found")
    }

    companion object {
        val Default = DayPrayersInfo(
            listOf(
                PrayerInfo(
                    prayer = Prayer.FAJR,
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.DUHA,
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.DHUHR,
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.ASR,
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.MAGHRIB,
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.ISHA,
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                )
            )
        )
    }
}
