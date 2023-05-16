package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.failure
import java.time.LocalDate
import javax.inject.Inject

class GetDayPrayers @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager
) {
    suspend fun call(
        date: LocalDate,
        forceUpdate: Boolean
    ): Result<DayPrayersInfo> {
        val location = appLocationManager.getLastKnownLocation()
            ?: return Result.failure("location can't be null")
        return prayersRepository.getDayPrayers(location, date, forceUpdate)
    }
}
