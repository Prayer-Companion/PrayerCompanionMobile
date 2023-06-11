package com.prayercompanion.prayercompanionandroid.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDateTime
import java.util.*

@Entity("PrayersInfo")
data class PrayerInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("prayer")
    val prayer: Prayer,
    @ColumnInfo("dateTime")
    val dateTime: LocalDateTime,
    @ColumnInfo("status")
    val status: PrayerStatus?
)