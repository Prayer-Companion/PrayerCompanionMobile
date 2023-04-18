package com.prayercompanion.prayercompanionandroid.data.local.mappers

import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import java.time.LocalDateTime

fun PrayerInfoEntity.toPrayerInfo(): PrayerInfo {
    return PrayerInfo(prayer = prayerName, dateTime = LocalDateTime.of(date, time), status = status)
}