package com.prayercompanion.prayercompanionandroid.data.local.db.mappers

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo

fun PrayerInfoEntity.toPrayerInfo(): PrayerInfo {
    return PrayerInfo(prayer = prayer, dateTime = dateTime, status = status)
}

fun List<PrayerInfoEntity>.toDayPrayerInfo(): DayPrayersInfo {
    return DayPrayersInfo(
        this.map {
            PrayerInfo(
                prayer = it.prayer,
                dateTime = it.dateTime,
                status = it.status
            )
        }
    )
}