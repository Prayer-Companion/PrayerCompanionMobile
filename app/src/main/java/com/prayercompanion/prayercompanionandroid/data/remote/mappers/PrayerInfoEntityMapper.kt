package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
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
            status = responseStatusToStatus(dayStatuses?.fajr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.DUHA,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.duhaTime.uppercase(), Consts.TimeFormatter),
            status = responseStatusToStatus(dayStatuses?.duha)
        ),
        PrayerInfoEntity(
            prayer = Prayer.DHUHR,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.dhuhrTime.uppercase(), Consts.TimeFormatter),
            status = responseStatusToStatus(dayStatuses?.dhuhr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.ASR,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.asrTime.uppercase(), Consts.TimeFormatter),
            status = responseStatusToStatus(dayStatuses?.asr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.MAGHRIB,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.maghribTime.uppercase(), Consts.TimeFormatter),
            status = responseStatusToStatus(dayStatuses?.maghrib)
        ),
        PrayerInfoEntity(
            prayer = Prayer.ISHA,
            date = LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
            time = LocalTime.parse(dayPrayerResponse.ishaTime.uppercase(), Consts.TimeFormatter),
            status = responseStatusToStatus(dayStatuses?.isha)
        )
    )
}