package com.prayercompanion.prayercompanionandroid.data.repositories

import com.prayercompanion.prayercompanionandroid.atEndOfDay
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.local.db.mappers.toDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.local.db.mappers.toPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.responsesToDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.responsesToPrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.Address
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.failure
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.util.TimeZone
import javax.inject.Inject

class PrayersRepositoryImpl @Inject constructor(
    private val prayerCompanionApi: PrayerCompanionApi,
    private val dao: PrayersInfoDao
) : PrayersRepository {

    override suspend fun getDayPrayers(
        location: Location?,
        address: Address?,
        dayDate: LocalDate,
        forceUpdate: Boolean
    ): Result<DayPrayersInfo> {
        return try {
            if (forceUpdate.not() || location == null) {
                val savedPrayers = dao.getPrayers(
                    startDateTime = dayDate.atStartOfDay(),
                    endDateTime = dayDate.atTime(LocalTime.MAX)
                )

                if (savedPrayers.isNotEmpty()) {
                    val dayPrayersInfo = savedPrayers.toDayPrayerInfo()
                    return Result.success(dayPrayersInfo)
                }
            }

            if (location == null) {
                return Result.failure("Failed to get day prayers: Location is null")
            }

            val yearMonth = YearMonth.from(dayDate)
            val (prayersResponse, statusesResponse) = loadMonthPrayers(yearMonth, location, address)

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

    override suspend fun getPrayer(
        prayer: Prayer,
        date: LocalDate,
        location: Location?,
        address: Address?
    ): Result<PrayerInfo> {
        val prayerInfo = dao.getPrayer(prayer, date)?.toPrayerInfo()
        return if (prayerInfo != null) {
            Result.success(prayerInfo)
        } else {

            if (location == null) {
                return Result.failure("Failed to get prayer: Location is null")
            }

            val yearMonth = YearMonth.from(date)
            val (prayersResponse, statusesResponse) = loadMonthPrayers(yearMonth, location, address)

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

    //todo what if last week doesn't exist?
    override fun getStatusesByDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<PrayerStatus?>> {
        return dao.getPrayersStatusesByDate(startDateTime, endDateTime)
    }

    private suspend fun loadMonthPrayers(
        yearMonth: YearMonth,
        location: Location,
        address: Address?
    ): Pair<List<DayPrayerResponse>, List<DayPrayerStatusResponse>> {
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()

        val prayersResponse = prayerCompanionApi.getPrayers(
            timeZone = TimeZone.getDefault().id,
            latitude = location.latitude.toString(),
            longitude = location.longitude.toString(),
            countryCode = address?.countryCode,
            cityName = address?.locality,
            monthOfYear = Consts.MonthYearFormatter.format(yearMonth)
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

        val prayersWithStatuses: List<PrayerInfoEntity> = prayersResponse.flatMap { dayPrayers ->
            val dayStatuses = statusesResponse.find { status -> status.date == dayPrayers.date }
            responsesToPrayerInfoEntity(dayPrayers, dayStatuses)
        }

        dao.deleteOldAndInsertNewTransaction(
            startOfMonth.atStartOfDay(),
            endOfMonth.atEndOfDay(),
            prayersWithStatuses
        )
    }

    //Client to Backend values mapper
    private fun prayerStatusToString(prayerStatus: PrayerStatus?): String {
        return when (prayerStatus) {
            PrayerStatus.Jamaah -> "jamaah"
            PrayerStatus.OnTime -> "onTime"
            PrayerStatus.AfterHalfTime -> "afterHalfTime"
            PrayerStatus.Late -> "late"
            PrayerStatus.Qadaa -> "missed"
            PrayerStatus.Missed -> "qadaa"
            else -> "none"
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