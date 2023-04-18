package com.prayercompanion.prayercompanionandroid.domain.usecases

import android.location.Location
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class GetCurrentPrayer @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val clock: Clock
) {

    suspend fun call(
        location: Location,
    ): Result<PrayerInfo> {
        val currentDate = LocalDate.now(clock)
        val currentTime = LocalTime.now(clock)

        val prayers = prayersRepository.getDayPrayers(location, currentDate)

        return prayers.map {
            if (currentTime in LocalTime.MIN.rangeTo(it.get(Prayer.FAJR).time)) {
                return@map prayersRepository.getPrayer(Prayer.ISHA, currentDate.minusDays(1))
            }
            if (currentTime in it.get(Prayer.ISHA).time.rangeTo(LocalTime.MAX)) {
                return@map it.get(Prayer.ISHA)
            }

            it.prayers.take(it.prayers.size - 1).forEachIndexed { index, prayerInfo ->
                val currentPrayerTime = prayerInfo.time
                val nextPrayerTime = it.prayers[index + 1].time
                if (currentTime in currentPrayerTime.rangeTo(nextPrayerTime)) {
                    return@map prayerInfo
                }
            }
            throw Exception("Something went wrong while getting current prayer")
        }
    }
}