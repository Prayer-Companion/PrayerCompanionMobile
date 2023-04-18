package com.prayercompanion.prayercompanionandroid.domain

import android.location.Location
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PrayersFakeRepository : PrayersRepository {
    override suspend fun getDayPrayers(
        location: Location,
        date: LocalDate
    ): Result<DayPrayersInfo> {
        return Result.success(DEFAULT_DAY_PRAYERS_INFO)
    }

    override suspend fun getPrayer(prayer: Prayer, localDate: LocalDate): PrayerInfo {
        return DEFAULT_DAY_PRAYERS_INFO.get(prayer)
    }

    override suspend fun updatePrayerStatus(
        date: LocalDate,
        prayer: PrayerInfo,
        status: PrayerStatus
    ): Result<PrayerStatus> {
        TODO("Not yet implemented")
    }

    companion object {
        private val DEFAULT_DAY_PRAYERS_INFO = DayPrayersInfo(
            listOf(
                PrayerInfo(
                    prayer = Prayer.FAJR,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(5, 0)),
                    status = PrayerStatus.NotSet
                ),
                PrayerInfo(
                    prayer = Prayer.DUHA,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(7, 0)),
                    status = PrayerStatus.NotSet
                ),
                PrayerInfo(
                    prayer = Prayer.DHUHR,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(12, 0)),
                    status = PrayerStatus.NotSet
                ),
                PrayerInfo(
                    prayer = Prayer.ASR,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(15, 0)),
                    status = PrayerStatus.NotSet
                ),
                PrayerInfo(
                    prayer = Prayer.MAGHRIB,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(18, 0)),
                    status = PrayerStatus.NotSet
                ),
                PrayerInfo(
                    prayer = Prayer.ISHA,
                    dateTime = LocalDateTime.of(Consts.TODAY_DATE, LocalTime.of(20, 0)),
                    status = PrayerStatus.NotSet
                )
            )
        )
    }
}

