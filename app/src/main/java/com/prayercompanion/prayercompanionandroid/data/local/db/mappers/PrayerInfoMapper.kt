package com.prayercompanion.prayercompanionandroid.data.local.db.mappers

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.PrayerInfo

fun List<PrayerInfoEntity>.toDayPrayerInfo(): DayPrayersInfo {
    return DayPrayersInfo(
        this.map {
            PrayerInfo(
                prayer = it.prayer,
                dateTime = it.dateTime,
                selectedStatus = it.status,
                statusesWithTimeRanges = emptyList()
            )
        }
    )
}