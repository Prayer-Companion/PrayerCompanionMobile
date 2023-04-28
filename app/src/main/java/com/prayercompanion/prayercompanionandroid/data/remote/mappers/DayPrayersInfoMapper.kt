package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun DayPrayerResponse.toDayPrayerInfo(): DayPrayersInfo {
    val date = LocalDate.parse(date, Consts.DateFormatter)
    val fajrTime = LocalTime.parse(fajrTime.uppercase(), Consts.TimeFormatter)
    val duhaTime = LocalTime.parse(duhaTime.uppercase(), Consts.TimeFormatter)
    val dhuhrTime = LocalTime.parse(dhuhrTime.uppercase(), Consts.TimeFormatter)
    val asrTime = LocalTime.parse(asrTime.uppercase(), Consts.TimeFormatter)
    val maghribTime = LocalTime.parse(maghribTime.uppercase(), Consts.TimeFormatter)
    val ishaTime = LocalTime.parse(ishaTime.uppercase(), Consts.TimeFormatter)

    val prayers = listOf(
        PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime.of(date, fajrTime),
            status = PrayerStatus.NotSet
        ),  PrayerInfo(
            prayer = Prayer.DUHA,
            dateTime = LocalDateTime.of(date, duhaTime),
            status = PrayerStatus.NotSet
        ), PrayerInfo(
            prayer = Prayer.DHUHR,
            dateTime = LocalDateTime.of(date, dhuhrTime),
            status = PrayerStatus.NotSet
        ), PrayerInfo(
            prayer = Prayer.ASR,
            dateTime = LocalDateTime.of(date, asrTime),
            status = PrayerStatus.NotSet
        ), PrayerInfo(
            prayer = Prayer.MAGHRIB,
            dateTime = LocalDateTime.of(date, maghribTime),
            status = PrayerStatus.NotSet
        ), PrayerInfo(
            prayer = Prayer.ISHA,
            dateTime = LocalDateTime.of(date, ishaTime),
            status = PrayerStatus.NotSet
        )
    )

    return DayPrayersInfo(prayers)
}

