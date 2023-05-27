package com.prayercompanion.prayercompanionandroid.domain.repositories

import android.location.Location
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface PrayersRepository {

    suspend fun getDayPrayers(
        location: Location,
        dayDate: LocalDate,
        forceUpdate: Boolean = false
    ): Result<DayPrayersInfo>

    suspend fun getPrayer(
        prayer: Prayer,
        date: LocalDate,
        location: Location
    ): Result<PrayerInfo>

    suspend fun updatePrayerStatus(
        prayerInfo: PrayerInfo,
        prayerStatus: PrayerStatus
    ): Result<Unit>

    fun getStatusesByDate(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flow<List<PrayerStatus?>>
}