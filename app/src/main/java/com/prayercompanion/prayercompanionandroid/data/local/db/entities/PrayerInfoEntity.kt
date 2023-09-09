package com.prayercompanion.prayercompanionandroid.data.local.db.entities

import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDateTime

data class PrayerInfoEntity(
    val id: Int = 0,
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    val status: PrayerStatus
)