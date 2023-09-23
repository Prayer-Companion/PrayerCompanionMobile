package com.prayercompanion.prayercompanionandroid.data.repositories

import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.local.db.mappers.toDayPrayerInfo
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.remote.mappers.responsesToPrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.extensions.atEndOfDay
import com.prayercompanion.prayercompanionandroid.domain.extensions.atStartOfDay
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.exceptions.LocationMissingException
import com.prayercompanion.prayercompanionandroid.domain.utils.exceptions.UnknownException
import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address
import com.prayercompanion.shared.domain.models.app.YearMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.util.TimeZone

class PrayersRepositoryImpl constructor(
    private val prayerCompanionApi: PrayerCompanionApi,
    private val dao: PrayersInfoDao
) : PrayersRepository {

    override suspend fun loadAndSaveMonthlyPrayers(
        yearMonth: YearMonth,
        location: Location,
        address: Address?
    ): Result<Unit> {
        return try {
            val prayersResponse = prayerCompanionApi.getPrayers(
                timeZone = TimeZone.getDefault().id,
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString(),
                countryCode = address?.countryCode,
                cityName = address?.locality,
                monthOfYear = Consts.MonthYearFormatter.format(yearMonth.atDay(1))
            )

            val startOfMonth = yearMonth.atDay(1)
            val endOfMonth = yearMonth.atEndOfMonth()

            val statusesResponse = prayerCompanionApi.getPrayerStatuses(
                startDate = Consts.DateFormatter.format(startOfMonth),
                endDate = Consts.DateFormatter.format(endOfMonth)
            )
            insertMonthPrayers(startOfMonth, endOfMonth, prayersResponse, statusesResponse)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDayPrayersFromDB(
        dayDate: LocalDate
    ): DayPrayersInfo? {
        val savedPrayers = dao.getPrayers(
            startDateTime = dayDate.atStartOfDay(),
            endDateTime = dayDate.atEndOfDay()
        )

        return savedPrayers.takeIf { it.isNotEmpty() }?.toDayPrayerInfo()
    }

    override suspend fun getDayPrayers(
        location: Location?,
        address: Address?,
        dayDate: LocalDate
    ): Result<DayPrayersInfo> {
        getDayPrayersFromDB(dayDate)
            ?.let { return Result.success(it) }

        // if we couldn't get from the day prayers from DB,
        // then we load the month from backend and then query our local DB again

        val yearMonth = YearMonth.from(dayDate)

        if (location == null) {
            return Result.failure(LocationMissingException)
        }

        loadAndSaveMonthlyPrayers(yearMonth, location, address)
            .onFailure {
                return Result.failure(it)
            }

        getDayPrayersFromDB(dayDate)
            ?.let { return Result.success(it) }

        return Result.failure(UnknownException)
    }

    override suspend fun getDayPrayersFlow(
        location: Location?,
        address: Address?,
        dayDate: LocalDate
    ): Flow<Result<List<PrayerInfoEntity>>> {

        val savedDayPrayers = getDayPrayersFromDB(dayDate)

        if (savedDayPrayers == null) {
            // if we couldn't get from the day prayers from DB,
            // then we load the month from backend and then query our local DB again

            val yearMonth = YearMonth.from(dayDate)

            if (location == null) {
                return flowOf(Result.failure(LocationMissingException))
            }

            loadAndSaveMonthlyPrayers(yearMonth, location, address)
                .onFailure {
                    return flowOf(Result.failure(it))
                }
        }

        return getDayPrayersFromDBFlow(dayDate)
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

    private fun getDayPrayersFromDBFlow(
        dayDate: LocalDate
    ): Flow<Result<List<PrayerInfoEntity>>> {
        val savedPrayers = dao.getPrayersFlow(
            startDateTime = dayDate.atStartOfDay(),
            endDateTime = dayDate.atEndOfDay()
        )

        return savedPrayers.map {
            val info = it.takeIf { it.isNotEmpty() }
            if (info == null) {
                Result.failure(UnknownException)
            } else {
                Result.success(info)
            }
        }
    }

    private fun insertMonthPrayers(
        startOfMonth: LocalDate,
        endOfMonth: LocalDate,
        prayersResponse: List<DayPrayerResponse>,
        statusesResponse: List<DayPrayerStatusResponse>
    ) {
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
            PrayerStatus.None -> "none"
            null -> "none"
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