package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class GetNextPrayer @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val clock: Clock
) {

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun call(
        currentDayPrayersInfo: DayPrayersInfo
    ): Result<PrayerInfo> {
        val date = LocalDate.now(clock)
        val time = LocalTime.now(clock)

        if (time in LocalTime.MIN.rangeUntil(currentDayPrayersInfo.get(Prayer.FAJR).time)) {
            return Result.success(currentDayPrayersInfo.get(Prayer.FAJR))
        }
        if (time in currentDayPrayersInfo.get(Prayer.ISHA).time.rangeTo(LocalTime.MAX)) {
            return prayersRepository.getPrayer(Prayer.FAJR, date.plusDays(1))
        }

        currentDayPrayersInfo.prayers.take(currentDayPrayersInfo.prayers.size - 1)
            .forEachIndexed { index, prayerInfo ->
                if (time in prayerInfo.time.rangeUntil(currentDayPrayersInfo.prayers[index + 1].time)) {
                    return Result.success(currentDayPrayersInfo.prayers[index + 1])
                }
            }
        return Result.failure(Exception("Something went wrong while getting next prayer"))
    }
}