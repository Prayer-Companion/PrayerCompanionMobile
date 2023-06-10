package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.failure
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class GetCurrentPrayer @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager,
    private val clock: Clock
) {

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun call(): Result<PrayerInfo> {
        val location = appLocationManager.getLastKnownLocation()

        val address = appLocationManager.getAddress()

        val currentDate = LocalDate.now(clock)
        val currentTime = LocalTime.now(clock)

        val currentDayPrayersInfo = prayersRepository
            .getDayPrayers(location, address, currentDate)
            .getOrElse {
                return Result.failure(it)
            }

        if (currentTime in LocalTime.MIN.rangeUntil(currentDayPrayersInfo.get(Prayer.FAJR).time)) {
            return prayersRepository.getPrayer(
                prayer = Prayer.ISHA,
                date = currentDate.minusDays(1),
                location = location,
                address = address
            )
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

        return Result.failure("Something went wrong while getting current prayer")
    }
}