package com.prayercompanion.prayercompanionandroid.domain.usecases

import android.location.Location
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
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

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun call(
        currentDayPrayersInfo: DayPrayersInfo,
        location: Location
    ): Result<PrayerInfo> {
        val currentDate = LocalDate.now(clock)
        val currentTime = LocalTime.now(clock)

        if (currentTime in LocalTime.MIN.rangeUntil(currentDayPrayersInfo.get(Prayer.FAJR).time)) {
            return prayersRepository.getPrayer(Prayer.ISHA, currentDate.minusDays(1), location)
        }
        if (currentTime in currentDayPrayersInfo.get(Prayer.ISHA).time.rangeTo(LocalTime.MAX)) {
            return Result.success(currentDayPrayersInfo.get(Prayer.ISHA))
        }

        currentDayPrayersInfo.prayers.take(currentDayPrayersInfo.prayers.size - 1)
            .forEachIndexed { index, prayerInfo ->
                val currentPrayerTime = prayerInfo.time
                val nextPrayerTime = currentDayPrayersInfo.prayers[index + 1].time
                if (currentTime in currentPrayerTime.rangeUntil(nextPrayerTime)) {
                    return Result.success(prayerInfo)
                }
            }
        return Result.failure(Exception("Something went wrong while getting current prayer"))
    }
}