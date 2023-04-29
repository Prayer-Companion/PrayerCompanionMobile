package com.prayercompanion.prayercompanionandroid.domain.usecases

import android.location.Location
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
    suspend fun call(location: Location): Result<PrayerInfo> {
        val date = LocalDate.now(clock)
        val time = LocalTime.now(clock)

        val prayers = prayersRepository.getDayPrayers(location, date)

        return prayers.map {
            if (time in LocalTime.MIN.rangeUntil(it.get(Prayer.FAJR).time)) {
                return@map it.get(Prayer.FAJR)
            }
            if (time in it.get(Prayer.ISHA).time.rangeTo(LocalTime.MAX)) {
                return@map prayersRepository.getPrayer(Prayer.FAJR, date.plusDays(1))
            }

            it.prayers.take(it.prayers.size - 1).forEachIndexed { index, prayerInfo ->
                if (time in prayerInfo.time.rangeUntil(it.prayers[index + 1].time)) {
                    return@map it.prayers[index + 1]
                }
            }
            throw Exception("Something went wrong while getting next prayer")
        }
    }
}