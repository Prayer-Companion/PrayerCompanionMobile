package com.prayercompanion.prayercompanionandroid.domain.usecases

import android.location.Location
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.LocalDate
import javax.inject.Inject

class GetDayPrayers @Inject constructor(
    private val prayersRepository: PrayersRepository
) {
    suspend fun call(
        date: LocalDate,
        location: Location,
        forceUpdate: Boolean
    ): Result<DayPrayersInfo> {
        return prayersRepository.getDayPrayers(location, date, forceUpdate)
    }
}
