package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetDayPrayers @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager
) {
    suspend fun call(
        date: LocalDate
    ): Result<DayPrayersInfo> {
        val location = appLocationManager.getLastKnownLocation()
        val address = appLocationManager.getAddressByLocation(location)

        return prayersRepository.getDayPrayers(location, address, date)
    }

    suspend fun callFlow(
        date: LocalDate
    ): Flow<Result<DayPrayersInfo>> {
        val location = appLocationManager.getLastKnownLocation()
        val address = appLocationManager.getAddressByLocation(location)

        return prayersRepository.getDayPrayersFlow(location, address, date)
    }
}
