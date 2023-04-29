package com.prayercompanion.prayercompanionandroid.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity("PrayersInfo")
data class PrayerInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("prayer")
    val prayer: Prayer,
    @ColumnInfo("date")
    val date: LocalDate,
    @ColumnInfo("time")
    val time: LocalTime,
    @ColumnInfo("status")
    val status: PrayerStatus
)

fun List<PrayerInfoEntity>.toDayPrayerInfo(): DayPrayersInfo {
    return DayPrayersInfo(
        this.map {
            PrayerInfo(
                prayer = it.prayer,
                dateTime = LocalDateTime.of(it.date, it.time),
                status = it.status
            )
        }
    )
}