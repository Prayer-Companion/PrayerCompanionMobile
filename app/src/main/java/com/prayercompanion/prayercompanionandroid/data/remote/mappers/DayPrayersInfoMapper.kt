package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.toStatus
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun responsesToDayPrayerInfo(dayPrayerResponse: DayPrayerResponse, dayPrayerStatusResponse: DayPrayerStatusResponse?): DayPrayersInfo {
    val date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter)
    val fajrTime = LocalTime.parse(dayPrayerResponse.fajrTime.uppercase(), Consts.TimeFormatter)
    val duhaTime = LocalTime.parse(dayPrayerResponse.duhaTime.uppercase(), Consts.TimeFormatter)
    val dhuhrTime = LocalTime.parse(dayPrayerResponse.dhuhrTime.uppercase(), Consts.TimeFormatter)
    val asrTime = LocalTime.parse(dayPrayerResponse.asrTime.uppercase(), Consts.TimeFormatter)
    val maghribTime = LocalTime.parse(dayPrayerResponse.maghribTime.uppercase(), Consts.TimeFormatter)
    val ishaTime = LocalTime.parse(dayPrayerResponse.ishaTime.uppercase(), Consts.TimeFormatter)

    val prayers = listOf(
        PrayerInfo(
            prayer = Prayer.FAJR,
            dateTime = LocalDateTime.of(date, fajrTime),
            status = dayPrayerStatusResponse?.fajr.toStatus()
        ),  PrayerInfo(
            prayer = Prayer.DUHA,
            dateTime = LocalDateTime.of(date, duhaTime),
            status = dayPrayerStatusResponse?.duha.toStatus()
        ), PrayerInfo(
            prayer = Prayer.DHUHR,
            dateTime = LocalDateTime.of(date, dhuhrTime),
            status = dayPrayerStatusResponse?.dhuhr.toStatus()
        ), PrayerInfo(
            prayer = Prayer.ASR,
            dateTime = LocalDateTime.of(date, asrTime),
            status = dayPrayerStatusResponse?.asr.toStatus()
        ), PrayerInfo(
            prayer = Prayer.MAGHRIB,
            dateTime = LocalDateTime.of(date, maghribTime),
            status = dayPrayerStatusResponse?.maghrib.toStatus()
        ), PrayerInfo(
            prayer = Prayer.ISHA,
            dateTime = LocalDateTime.of(date, ishaTime),
            status = dayPrayerStatusResponse?.isha.toStatus()
        )
    )

    return DayPrayersInfo(prayers)
}

