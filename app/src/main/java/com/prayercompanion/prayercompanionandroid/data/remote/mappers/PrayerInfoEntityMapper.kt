package com.prayercompanion.prayercompanionandroid.data.remote.mappers

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse

import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.*
import java.time.LocalDate
import java.time.LocalTime

fun DayPrayerResponse.toPrayerInfoEntity(): List<PrayerInfoEntity> {
    return listOf(
        PrayerInfoEntity(
            prayerName = Prayer.FAJR,
            date = LocalDate.parse(date, Consts.DateFormatter),
            time = LocalTime.parse(fajrTime.uppercase(), Consts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.DUHA,
            date = LocalDate.parse(date, Consts.DateFormatter),
            time = LocalTime.parse(duhaTime.uppercase(), Consts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.DHUHR,
            date = LocalDate.parse(date, Consts.DateFormatter),
            time = LocalTime.parse(dhuhrTime.uppercase(), Consts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.ASR,
            date = LocalDate.parse(date, Consts.DateFormatter),
            time = LocalTime.parse(asrTime.uppercase(), Consts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.MAGHRIB,
            date = LocalDate.parse(date, Consts.DateFormatter),
            time = LocalTime.parse(maghribTime.uppercase(), Consts.TimeFormatter),
            status = PrayerStatus.NotSet
        ),
        PrayerInfoEntity(
            prayerName = Prayer.ISHA,
            date = LocalDate.parse(date, Consts.DateFormatter),
            time = LocalTime.parse(ishaTime.uppercase(), Consts.TimeFormatter),
            status = PrayerStatus.NotSet
        )
    )
}