package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.Address
import com.prayercompanion.prayercompanionandroid.domain.models.Location
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetPrayerStatusRanges @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager
) {

    suspend fun call(
        prayerDateTime: LocalDateTime,
        nextPrayer: Prayer?
    ): Map<PrayerStatus, OpenEndRange<LocalDateTime>>? {
        val location = appLocationManager.getLastKnownLocation()
        val address = appLocationManager.getAddressByLocation(location)

        return if (nextPrayer != null) {
            getRangesForSameDayPrayers(
                prayerDateTime = prayerDateTime,
                nextPrayer = nextPrayer,
                location = location,
                address = address
            )
        } else {
            getRangeForIsha(
                prayerDateTime = prayerDateTime,
                location = location,
                address = address
            )
        }
    }

    private suspend fun getRangesForSameDayPrayers(
        prayerDateTime: LocalDateTime,
        nextPrayer: Prayer,
        location: Location?,
        address: Address?
    ): Map<PrayerStatus, OpenEndRange<LocalDateTime>>? {
        val todayPrayers = prayersRepository.getDayPrayers(location, address, prayerDateTime.toLocalDate())

        todayPrayers
            .map { it.get(nextPrayer) }
            .onSuccess { nextPrayerInfo ->
                val fullTimeMinutes = ChronoUnit.MINUTES
                    .between(prayerDateTime, nextPrayerInfo.dateTime)

                return mapOf(
                    PrayerStatus.Late to run {
                        val start = fullTimeMinutes - LATE_REMAINING_TIME_IN_MINUTES

                        prayerDateTime.plusMinutes(start)..<prayerDateTime.plusMinutes(
                            fullTimeMinutes
                        )
                    },
                    PrayerStatus.AfterHalfTime to run {
                        val start = (fullTimeMinutes / 2)
                        val end = fullTimeMinutes - LATE_REMAINING_TIME_IN_MINUTES

                        prayerDateTime.plusMinutes(start)..<prayerDateTime.plusMinutes(end)
                    },
                    PrayerStatus.OnTime to run {
                        val end = fullTimeMinutes / 2

                        prayerDateTime..<prayerDateTime.plusMinutes(end)
                    },
                )
            }
        return null
    }

    private suspend fun getRangeForIsha(
        prayerDateTime: LocalDateTime,
        location: Location?,
        address: Address?,
    ): Map<PrayerStatus, OpenEndRange<LocalDateTime>>? {
        val date = prayerDateTime.toLocalDate()
        val todayPrayers = prayersRepository.getDayPrayers(location, address, date)
        val nextDayPrayers =
            prayersRepository.getDayPrayers(location, address, date.plusDays(1))

        val maghribDateTime =
            todayPrayers
                .map { it.get(Prayer.MAGHRIB) }
                .getOrNull()?.dateTime ?: return null

        val fajr = nextDayPrayers
            .map { it.get(Prayer.FAJR) }
            .getOrNull() ?: return null

        // The night in islam is the time between Maghrib and Fajr
        val nightTimeInMinutes = ChronoUnit.MINUTES
            .between(maghribDateTime, fajr.dateTime)

        return mapOf(
            // After middle of the night
            PrayerStatus.Late to run {
                val start = nightTimeInMinutes / 2

                maghribDateTime.plusMinutes(start)..<fajr.dateTime
            },
            // Between first third of the night and middle of the night
            PrayerStatus.AfterHalfTime to run {
                val start = nightTimeInMinutes / 3
                val end = nightTimeInMinutes / 2

                maghribDateTime.plusMinutes(start)..<maghribDateTime.plusMinutes(end)
            },
            // Before first third of the night
            PrayerStatus.OnTime to run {
                val firstThirdOfNight = nightTimeInMinutes / 3

                prayerDateTime..<maghribDateTime.plusMinutes(firstThirdOfNight)
            },
        )
    }

    companion object {
        private const val LATE_REMAINING_TIME_IN_MINUTES = 15
    }

}