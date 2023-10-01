package com.prayercompanion.prayercompanionandroid.domain.utils

import com.prayercompanion.prayercompanionandroid.domain.Consts
import com.prayercompanion.shared.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.app.Address
import com.prayercompanion.shared.domain.models.app.YearMonth
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class PrayersFakeRepository : PrayersRepository {
    override suspend fun getDayPrayers(
        location: Location?,
        address: Address?,
        dayDate: LocalDate
    ): Result<DayPrayersInfo> {
        return Result.success(DEFAULT_DAY_PRAYERS_INFO)
    }

    override fun getStatusesByDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<PrayerStatus?>> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePrayerStatus(
        prayerInfo: PrayerInfo,
        prayerStatus: PrayerStatus
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getDayPrayersFromDB(dayDate: LocalDate): DayPrayersInfo? {
        TODO("Not yet implemented")
    }

    override suspend fun loadAndSaveMonthlyPrayers(
        yearMonth: YearMonth,
        location: Location,
        address: Address?
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getDayPrayersFlow(
        location: Location?,
        address: Address?,
        dayDate: LocalDate
    ): Flow<Result<List<PrayerInfoEntity>>> {
        TODO("Not yet implemented")
    }

    companion object {
        fun getFakeDayPrayersInfo(date: LocalDate): DayPrayersInfo {
            return DayPrayersInfo(
                listOf(
                    PrayerInfo(
                        prayer = Prayer.FAJR,
                        dateTime = LocalDateTime(date, LocalTime(4, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.DUHA,
                        dateTime = LocalDateTime(date, LocalTime(7, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.DHUHR,
                        dateTime = LocalDateTime(date, LocalTime(12, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.ASR,
                        dateTime = LocalDateTime(date, LocalTime(15, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.MAGHRIB,
                        dateTime = LocalDateTime(date, LocalTime(18, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.ISHA,
                        dateTime = LocalDateTime(date, LocalTime(20, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    )
                )
            )
        }

        val DEFAULT_DAY_PRAYERS_INFO = DayPrayersInfo(
            listOf(
                PrayerInfo(
                    prayer = Prayer.FAJR,
                    dateTime = LocalDateTime(Consts.TODAY_DATE, LocalTime(5, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.DUHA,
                    dateTime = LocalDateTime(Consts.TODAY_DATE, LocalTime(7, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.DHUHR,
                    dateTime = LocalDateTime(Consts.TODAY_DATE, LocalTime(12, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.ASR,
                    dateTime = LocalDateTime(Consts.TODAY_DATE, LocalTime(15, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.MAGHRIB,
                    dateTime = LocalDateTime(Consts.TODAY_DATE, LocalTime(18, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.ISHA,
                    dateTime = LocalDateTime(Consts.TODAY_DATE, LocalTime(20, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                )
            )
        )
    }
}

