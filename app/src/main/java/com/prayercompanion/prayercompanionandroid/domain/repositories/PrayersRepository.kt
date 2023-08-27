package com.prayercompanion.prayercompanionandroid.domain.repositories

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.Address
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

interface PrayersRepository {

    suspend fun getDayPrayersFromDB(
        dayDate: LocalDate
    ): DayPrayersInfo?

    suspend fun getDayPrayers(
        location: Location?,
        address: Address?,
        dayDate: LocalDate
    ): Result<DayPrayersInfo>

    suspend fun updatePrayerStatus(
        prayerInfo: PrayerInfo,
        prayerStatus: PrayerStatus
    ): Result<Unit>

    fun getStatusesByDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<PrayerStatus?>>

    suspend fun loadAndSaveMonthlyPrayers(
        yearMonth: YearMonth,
        location: Location,
        address: Address?
    ): Result<Unit>

    suspend fun getDayPrayersFlow(
        location: Location?,
        address: Address?,
        dayDate: LocalDate
    ): Flow<Result<List<PrayerInfoEntity>>>
}