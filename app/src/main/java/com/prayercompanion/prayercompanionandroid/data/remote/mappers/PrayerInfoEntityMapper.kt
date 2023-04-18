package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse

import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.utils.DataConsts
import com.prayercompanion.prayercompanionandroid.domain.models.*
import java.time.LocalDate
import java.time.LocalTime

fun DayPrayerResponse.toPrayerInfoEntity(): List<PrayerInfoEntity> {
    return listOf(
        PrayerInfoEntity(
            prayerName = Prayer.FAJR,
            date = LocalDate.parse(date, DataConsts.DateFormatter),
            time = LocalTime.parse(fajrTime.uppercase(), DataConsts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.DUHA,
            date = LocalDate.parse(date, DataConsts.DateFormatter),
            time = LocalTime.parse(duhaTime.uppercase(), DataConsts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.DHUHR,
            date = LocalDate.parse(date, DataConsts.DateFormatter),
            time = LocalTime.parse(dhuhrTime.uppercase(), DataConsts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.ASR,
            date = LocalDate.parse(date, DataConsts.DateFormatter),
            time = LocalTime.parse(asrTime.uppercase(), DataConsts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.MAGHRIB,
            date = LocalDate.parse(date, DataConsts.DateFormatter),
            time = LocalTime.parse(maghribTime.uppercase(), DataConsts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.ISHA,
            date = LocalDate.parse(date, DataConsts.DateFormatter),
            time = LocalTime.parse(ishaTime.uppercase(), DataConsts.TimeFormatter),
            status = PrayerStatus.NotSet
        )
    )
}