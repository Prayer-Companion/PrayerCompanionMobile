package com.prayercompanion.prayercompanionandroid.domain.repositories

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.app.Address
import com.prayercompanion.shared.domain.models.app.YearMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

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