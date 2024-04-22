package com.prayercompanion.shared.data.local.db.entities

import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import kotlinx.datetime.LocalDateTime

data class PrayerInfoEntity(
    val id: Int = 0,
    val prayer: Prayer,
    val dateTime: LocalDateTime,
    val status: PrayerStatus
)