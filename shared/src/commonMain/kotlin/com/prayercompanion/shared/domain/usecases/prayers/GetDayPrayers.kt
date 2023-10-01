package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.utils.AppLocationManager
import kotlinx.datetime.LocalDate

class GetDayPrayers constructor(
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

}