package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.toStatus
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import java.time.LocalDate
import java.time.LocalTime

fun responsesToPrayerInfoEntity(
    dayPrayerResponse: DayPrayerResponse,
    dayStatuses: DayPrayerStatusResponse?
): List<PrayerInfoEntity> {
    return listOf(
        PrayerInfoEntity(
            prayer = Prayer.FAJR,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.fajrTime.uppercase(), Consts.TimeFormatter),
            status = dayStatuses?.fajr.toStatus()
        ),
        PrayerInfoEntity(
            prayer = Prayer.DUHA,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.duhaTime.uppercase(), Consts.TimeFormatter),
            status = dayStatuses?.duha.toStatus()
        ),
        PrayerInfoEntity(
            prayer = Prayer.DHUHR,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.dhuhrTime.uppercase(), Consts.TimeFormatter),
            status = dayStatuses?.dhuhr.toStatus()
        ),
        PrayerInfoEntity(
            prayer = Prayer.ASR,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.asrTime.uppercase(), Consts.TimeFormatter),
            status = dayStatuses?.asr.toStatus()
        ),
        PrayerInfoEntity(
            prayer = Prayer.MAGHRIB,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.maghribTime.uppercase(), Consts.TimeFormatter),
            status = dayStatuses?.maghrib.toStatus()
        ),
        PrayerInfoEntity(
            prayer = Prayer.ISHA,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.ishaTime.uppercase(), Consts.TimeFormatter),
            status = dayStatuses?.isha.toStatus()
        )
    )
}