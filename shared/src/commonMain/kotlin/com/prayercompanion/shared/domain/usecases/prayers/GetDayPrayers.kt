package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.repositories.LocationRepository
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import kotlinx.datetime.LocalDate

class GetDayPrayers constructor(
    private val prayersRepository: PrayersRepository,
    private val locationRepository: LocationRepository
) {
    suspend fun call(
        date: LocalDate
    ): Result<DayPrayersInfo> {
        val location = locationRepository.getLastKnownLocation()
        val address = locationRepository.getAddressByLocation(location)

        return prayersRepository.getDayPrayers(location, address, date)
    }

}