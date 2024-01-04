package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.models.DailyPrayersCombo
import com.prayercompanion.shared.domain.models.app.YearMonth
import com.prayercompanion.shared.domain.repositories.LocationRepository
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.domain.utils.exceptions.LocationMissingException
import com.prayercompanion.shared.domain.utils.exceptions.UnknownException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class GetDailyPrayersCombo constructor(
    private val locationRepository: LocationRepository,
    private val prayersRepository: PrayersRepository,
    private val isConnectedToInternet: IsConnectedToInternet
) {

    suspend fun call(): Flow<DailyPrayersCombo> {
        val currentDate = LocalDate.now()
        val yearMonth = YearMonth.from(currentDate)
        val location = locationRepository.getLastKnownLocation()
        val address = locationRepository.getAddressByLocation(location)

        return flow {
            getDailyPrayersInfoComboFromDB()?.let {
                emit(it)
            }

            if (location == null) {
                error(LocationMissingException)
            }

            if (isConnectedToInternet.call().not()) {
                return@flow
            }

            suspend fun loadMonthPrayers(yearMonth: YearMonth) {
                prayersRepository.loadAndSaveMonthlyPrayers(
                    yearMonth = yearMonth,
                    location = location,
                    address = address
                )
            }

            if (currentDate == yearMonth.atEndOfMonth()) {
                loadMonthPrayers(yearMonth.plusMonths(1))
            }
            loadMonthPrayers(yearMonth)
            if (currentDate.dayOfMonth == 1) {
                loadMonthPrayers(yearMonth.minusMonths(1))
            }

            val dailyPrayersCombo = getDailyPrayersInfoComboFromDB()
                ?: error(UnknownException)
            emit(dailyPrayersCombo)
        }
    }

    private suspend fun getDailyPrayersInfoComboFromDB(): DailyPrayersCombo? {
        val today = LocalDate.now()
        val yesterday = today.minus(1, DateTimeUnit.DAY)
        val tomorrow = today.plus(1, DateTimeUnit.DAY)

        val todayPrayersInfo = prayersRepository.getDayPrayersFromDB(today) ?: return null
        val yesterdayPrayersInfo = prayersRepository.getDayPrayersFromDB(yesterday)
        val tomorrowPrayersInfo = prayersRepository.getDayPrayersFromDB(tomorrow)

        return DailyPrayersCombo(
            todayPrayersInfo,
            yesterdayPrayersInfo,
            tomorrowPrayersInfo
        )
    }
}