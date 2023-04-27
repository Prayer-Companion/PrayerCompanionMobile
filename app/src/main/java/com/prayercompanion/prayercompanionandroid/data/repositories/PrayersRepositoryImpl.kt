package com.prayercompanion.prayercompanionandroid.data.repositories

import android.location.Location
import com.prayercompanion.prayercompanionandroid.data.local.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.entities.toDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.local.mappers.toPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.toDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.toPrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.*
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class PrayersRepositoryImpl @Inject constructor(
    private val prayerCompanionApi: PrayerCompanionApi,
    private val dao: PrayersInfoDao
) : PrayersRepository {

    override suspend fun getDayPrayers(
        location: Location,
        date: LocalDate
    ): Result<DayPrayersInfo> {

        return try {
            Result.success(getTodayPrayers(location, date))
        } catch (e: Exception) {
            e.printStackTraceInDebug()
            Result.failure(e)
        }
    }

    private suspend fun getTodayPrayers(location: Location, date: LocalDate): DayPrayersInfo {
        val savedPrayers = dao.getPrayers(date)
        if (savedPrayers.isNotEmpty()) {
            return savedPrayers.toDayPrayerInfo()
        }

        val response = prayerCompanionApi.getPrayers(
            TimeZone.getDefault().id,
            location.latitude.toString(),
            location.longitude.toString(),
            date.format(Consts.MonthYearFormatter)
        )

        insertMonthPrayers(response, date)

        val dayPrayers = response
            .find { it.date == date.format(Consts.DateFormatter) }
            ?: throw Exception(
                "Response from BE doesn't have the required date ${
                    date.format(
                        Consts.MonthYearFormatter
                    )
                }"
            )

        return dayPrayers.toDayPrayerInfo()
    }

    private fun insertMonthPrayers(
        response: List<DayPrayerResponse>,
        date: LocalDate
    ) {
        dao.delete(
            LocalDate.of(date.year, date.monthValue, 1),
            LocalDate.of(date.year, date.monthValue + 1, 1)
        )
        dao.insertAll(response.map { it.toPrayerInfoEntity() }.flatten())
    }

    override suspend fun getPrayer(prayer: Prayer, date: LocalDate): PrayerInfo {
        return dao.getPrayer(prayer, date).toPrayerInfo()
    }

    override suspend fun updatePrayerStatus(
        date: LocalDate,
        prayer: PrayerInfo,
        status: PrayerStatus
    ): Result<PrayerStatus> {
        return Result.success(PrayerStatus.Jamaah)
    }
}