package com.prayercompanion.prayercompanionandroid.domain.utils

import com.prayercompanion.prayercompanionandroid.domain.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.models.Address
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PrayersFakeRepository : PrayersRepository {
    override suspend fun getDayPrayers(
        location: Location?,
        address: Address?,
        dayDate: LocalDate,
        forceUpdate: Boolean
    ): Result<DayPrayersInfo> {
        return Result.success(DEFAULT_DAY_PRAYERS_INFO)
    }

    override suspend fun getPrayer(
        prayer: Prayer,
        date: LocalDate,
        location: Location?,
        address: Address?
    ): Result<PrayerInfo> {
        return Result.success(getFakeDayPrayersInfo(date).get(prayer))
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

    companion object {
        fun getFakeDayPrayersInfo(date: LocalDate): DayPrayersInfo {
            return DayPrayersInfo(
                listOf(
                    PrayerInfo(
                        prayer = Prayer.FAJR,
                        dateTime = LocalDateTime.of(date, LocalTime.of(4, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.DUHA,
                        dateTime = LocalDateTime.of(date, LocalTime.of(7, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.DHUHR,
                        dateTime = LocalDateTime.of(date, LocalTime.of(12, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.ASR,
                        dateTime = LocalDateTime.of(date, LocalTime.of(15, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.MAGHRIB,
                        dateTime = LocalDateTime.of(date, LocalTime.of(18, 0)),
                        selectedStatus = PrayerStatus.None,
                        statusesWithTimeRanges = emptyList()
                    ),
                    PrayerInfo(
                        prayer = Prayer.ISHA,
                        dateTime = LocalDateTime.of(date, LocalTime.of(20, 0)),
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
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(5, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.DUHA,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(7, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.DHUHR,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(12, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.ASR,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(15, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.MAGHRIB,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(18, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                ),
                PrayerInfo(
                    prayer = Prayer.ISHA,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(20, 0)),
                    selectedStatus = PrayerStatus.None,
                    statusesWithTimeRanges = emptyList()
                )
            )
        )
    }
}

