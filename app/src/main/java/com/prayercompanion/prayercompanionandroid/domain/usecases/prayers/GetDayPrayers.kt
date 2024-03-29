package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import java.time.LocalDate

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
