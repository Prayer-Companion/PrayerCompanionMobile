package com.prayercompanion.prayercompanionandroid.data.repositories

import android.location.Location
import com.prayercompanion.prayercompanionandroid.data.local.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.local.entities.toDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.local.mappers.toPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.responsesToDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.responsesToPrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.failure
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import com.skydoves.whatif.whatIfNotNull
import java.time.LocalDate
import java.time.YearMonth
import java.util.TimeZone
import javax.inject.Inject

class PrayersRepositoryImpl @Inject constructor(
    private val prayerCompanionApi: PrayerCompanionApi,
    private val dao: PrayersInfoDao
) : PrayersRepository {

    override suspend fun getDayPrayers(
        location: Location,
        dayDate: LocalDate,
        forceUpdate: Boolean
    ): Result<DayPrayersInfo> {
        return try {
            if (forceUpdate.not()) {
                val savedPrayers = dao.getPrayers(dayDate)
                if (savedPrayers.isNotEmpty()) {
                    val dayPrayersInfo = savedPrayers.toDayPrayerInfo()
                    return Result.success(dayPrayersInfo)
                }
            }

            val yearMonth = YearMonth.from(dayDate)
            val (prayersResponse, statusesResponse) = loadMonthPrayers(yearMonth, location)

            val dayPrayer = dayPrayerResponseAndStatusResponseToDayPrayerInfo(
                dayDate,
                prayersResponse,
                statusesResponse
            )

            Result.success(dayPrayer)
        } catch (e: Exception) {
            e.printStackTraceInDebug()
            Result.failure(e)
        }
    }

    private suspend fun loadMonthPrayers(
        yearMonth: YearMonth,
        location: Location
    ): Pair<List<DayPrayerResponse>, List<DayPrayerStatusResponse>> {
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()

        val prayersResponse = prayerCompanionApi.getPrayers(
            TimeZone.getDefault().id,
            location.latitude.toString(),
            location.longitude.toString(),
            Consts.MonthYearFormatter.format(yearMonth)
        )

        val statusesResponse = prayerCompanionApi.getPrayerStatuses(
            startDate = Consts.DateFormatter.format(startOfMonth),
            endDate = Consts.DateFormatter.format(endOfMonth)
        )

        insertMonthPrayers(startOfMonth, endOfMonth, prayersResponse, statusesResponse)

        return prayersResponse to statusesResponse
    }

    private fun dayPrayerResponseAndStatusResponseToDayPrayerInfo(
        dayDate: LocalDate,
        prayersResponse: List<DayPrayerResponse>,
        statusesResponse: List<DayPrayerStatusResponse>
    ): DayPrayersInfo {

        prayersResponse
            .find { it.date == dayDate.format(Consts.DateFormatter) }
            .whatIfNotNull { dayPrayers ->

                val dayStatus = statusesResponse
                    .find { it.date == dayDate.format(Consts.DateFormatter) }

                return responsesToDayPrayerInfo(dayPrayers, dayStatus)
            }

        throw Exception(
            "Response from BE doesn't have the required date ${dayDate.format(Consts.MonthYearFormatter)}"
        )

    }

    private fun insertMonthPrayers(
        startOfMonth: LocalDate,
        endOfMonth: LocalDate,
        prayersResponse: List<DayPrayerResponse>,
        statusesResponse: List<DayPrayerStatusResponse>
    ) {
        var currentDate = startOfMonth
        val dates = mutableListOf<LocalDate>()
        while (!currentDate.isAfter(endOfMonth)) {
            dates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        dao.delete(dates)

        val prayersWithStatuses: List<PrayerInfoEntity> = prayersResponse.flatMap { dayPrayers ->
            val dayStatuses = statusesResponse.find { status -> status.date == dayPrayers.date }
            responsesToPrayerInfoEntity(dayPrayers, dayStatuses)
        }

        dao.insertAll(prayersWithStatuses)
    }

    override suspend fun getPrayer(
        prayer: Prayer,
        date: LocalDate,
        location: Location
    ): Result<PrayerInfo> {
        val prayerInfo = dao.getPrayer(prayer, date)?.toPrayerInfo()
        return if (prayerInfo != null) {
            Result.success(prayerInfo)
        } else {

            val yearMonth = YearMonth.from(date)
            val (prayersResponse, statusesResponse) = loadMonthPrayers(yearMonth, location)

            try {
                val dayPrayers = dayPrayerResponseAndStatusResponseToDayPrayerInfo(
                    date,
                    prayersResponse,
                    statusesResponse
                )

                Result.success(dayPrayers.get(prayer))
            } catch (e: Exception) {
                Result.failure("Prayer Doesn't Exist")
            }
        }
    }

    override suspend fun updatePrayerStatus(
        prayerInfo: PrayerInfo,
        prayerStatus: PrayerStatus
    ): Result<Unit> {
        val prayerDateStr = Consts.DateFormatter.format(prayerInfo.date)
        val prayerStatusStr = prayerStatusToString(prayerStatus)
        val prayerName = prayerToPrayerNameString(prayerInfo.prayer)

        return try {
            prayerCompanionApi.updatePrayerStatus(
                prayerDateStr,
                prayerName,
                prayerStatusStr
            )

            dao.updatePrayerStatus(
                prayer = prayerInfo.prayer,
                date = prayerInfo.date,
                status = prayerStatus
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Client to Backend values mapper
    private fun prayerStatusToString(prayerStatus: PrayerStatus): String {
        return when (prayerStatus) {
            PrayerStatus.Jamaah -> "jamaah"
            PrayerStatus.OnTime -> "onTime"
            PrayerStatus.Late -> "late"
            PrayerStatus.Qadaa -> "missed"
            PrayerStatus.Missed -> "qadaa"
            PrayerStatus.NotSet -> "none"
        }
    }

    private fun prayerToPrayerNameString(prayer: Prayer): String {
        return when (prayer) {
            Prayer.FAJR -> "fajr"
            Prayer.DUHA -> "sunrise"
            Prayer.DHUHR -> "dhuhr"
            Prayer.ASR -> "asr"
            Prayer.MAGHRIB -> "maghrib"
            Prayer.ISHA -> "isha"
        }
    }
}