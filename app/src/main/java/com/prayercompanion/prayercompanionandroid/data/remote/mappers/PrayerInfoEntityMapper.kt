package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun responsesToPrayerInfoEntity(
    dayPrayerResponse: DayPrayerResponse,
    dayStatuses: DayPrayerStatusResponse?
): List<PrayerInfoEntity> {
    val fajrDateTime = LocalDateTime.of(
        LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
        LocalTime.parse(dayPrayerResponse.fajrTime.uppercase(), Consts.TimeFormatter)
    )
    val duhaDateTime = LocalDateTime.of(
        LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
        LocalTime.parse(dayPrayerResponse.duhaTime.uppercase(), Consts.TimeFormatter),
    )
    val dhuhrDateTime = LocalDateTime.of(
        LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
        LocalTime.parse(dayPrayerResponse.dhuhrTime.uppercase(), Consts.TimeFormatter),
    )
    val asrDateTime = LocalDateTime.of(
        LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
        LocalTime.parse(dayPrayerResponse.asrTime.uppercase(), Consts.TimeFormatter),
    )
    val maghribDateTime = LocalDateTime.of(
        LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
        LocalTime.parse(dayPrayerResponse.maghribTime.uppercase(), Consts.TimeFormatter),
    )
    val ishaDateTime = LocalDateTime.of(
        LocalDate.parse(dayPrayerResponse.date, Consts.DateFormatter),
        LocalTime.parse(dayPrayerResponse.ishaTime.uppercase(), Consts.TimeFormatter),
    )


    return listOf(
        PrayerInfoEntity(
            prayer = Prayer.FAJR,
            dateTime = fajrDateTime,
            status = responseStatusToStatus(dayStatuses?.fajr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.DUHA,
            dateTime = duhaDateTime,
            status = responseStatusToStatus(dayStatuses?.duha)
        ),
        PrayerInfoEntity(
            prayer = Prayer.DHUHR,
            dateTime = dhuhrDateTime,
            status = responseStatusToStatus(dayStatuses?.dhuhr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.ASR,
            dateTime = asrDateTime,
            status = responseStatusToStatus(dayStatuses?.asr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.MAGHRIB,
            dateTime = maghribDateTime,
            status = responseStatusToStatus(dayStatuses?.maghrib)
        ),
        PrayerInfoEntity(
            prayer = Prayer.ISHA,
            dateTime = ishaDateTime,
            status = responseStatusToStatus(dayStatuses?.isha)
        )
    )
}