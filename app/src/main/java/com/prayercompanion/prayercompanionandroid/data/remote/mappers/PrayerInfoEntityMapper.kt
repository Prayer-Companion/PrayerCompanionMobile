package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import kotlinx.datetime.LocalDateTime

fun responsesToPrayerInfoEntity(
    dayPrayerResponse: DayPrayerResponse,
    dayStatuses: DayPrayerStatusResponse?
): List<PrayerInfoEntity> {
    val fajrDateTime = LocalDateTime(
        Consts.DateFormatter.parseToLocalDate(dayPrayerResponse.date),
        Consts.TimeFormatter.parseToLocalTime(dayPrayerResponse.fajrTime.uppercase())
    )
    val sunriseDateTime = LocalDateTime(
        Consts.DateFormatter.parseToLocalDate(dayPrayerResponse.date),
        Consts.TimeFormatter.parseToLocalTime(dayPrayerResponse.sunriseTime.uppercase())
    )
    val dhuhrDateTime = LocalDateTime(
        Consts.DateFormatter.parseToLocalDate(dayPrayerResponse.date),
        Consts.TimeFormatter.parseToLocalTime(dayPrayerResponse.dhuhrTime.uppercase())
    )
    val asrDateTime = LocalDateTime(
        Consts.DateFormatter.parseToLocalDate(dayPrayerResponse.date),
        Consts.TimeFormatter.parseToLocalTime(dayPrayerResponse.asrTime.uppercase())
    )
    val maghribDateTime = LocalDateTime(
        Consts.DateFormatter.parseToLocalDate(dayPrayerResponse.date),
        Consts.TimeFormatter.parseToLocalTime(dayPrayerResponse.maghribTime.uppercase())
    )
    val ishaDateTime = LocalDateTime(
        Consts.DateFormatter.parseToLocalDate(dayPrayerResponse.date),
        Consts.TimeFormatter.parseToLocalTime(dayPrayerResponse.ishaTime.uppercase())
    )


    return listOf(
        PrayerInfoEntity(
            prayer = Prayer.FAJR,
            dateTime = fajrDateTime,
            status = responseStatusToStatus(dayStatuses?.fajr)
        ),
        PrayerInfoEntity(
            prayer = Prayer.DUHA,
            dateTime = sunriseDateTime,
            status = responseStatusToStatus(dayStatuses?.sunrise)
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