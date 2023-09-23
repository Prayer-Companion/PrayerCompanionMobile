package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.extensions.instantBetween
import com.prayercompanion.prayercompanionandroid.domain.extensions.plus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.app.Address
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class GetPrayerStatusRanges constructor(
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
        val todayPrayers = prayersRepository.getDayPrayers(location, address, prayerDateTime.date)

        todayPrayers
            .map { it.get(nextPrayer) }
            .onSuccess { nextPrayerInfo ->

                val fullTimeMinutes = instantBetween(prayerDateTime, nextPrayerInfo.dateTime).inWholeMinutes

                return mapOf(
                    PrayerStatus.Late to run {
                        val start = fullTimeMinutes - LATE_REMAINING_TIME_IN_MINUTES
                        val startingDateTime = prayerDateTime
                            .plus(start, DateTimeUnit.MINUTE)
                        val endingDateTime = prayerDateTime
                            .plus(fullTimeMinutes, DateTimeUnit.MINUTE)

                        startingDateTime..<endingDateTime
                    },
                    PrayerStatus.AfterHalfTime to run {
                        val start = (fullTimeMinutes / 2)
                        val end = fullTimeMinutes - LATE_REMAINING_TIME_IN_MINUTES
                        val startingDateTime = prayerDateTime.plus(start, DateTimeUnit.MINUTE)
                        val endingDateTime = prayerDateTime.plus(end, DateTimeUnit.MINUTE)

                        startingDateTime..<endingDateTime
                    },
                    PrayerStatus.OnTime to run {
                        val end = fullTimeMinutes / 2

                        prayerDateTime..<prayerDateTime.plus(end, DateTimeUnit.MINUTE)
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
        val date = prayerDateTime.date
        val todayPrayers = prayersRepository.getDayPrayers(location, address, date)
        val nextDayPrayers =
            prayersRepository.getDayPrayers(location, address, date.plus(1, DateTimeUnit.DAY))

        val maghribDateTime =
            todayPrayers
                .map { it.get(Prayer.MAGHRIB) }
                .getOrNull()?.dateTime ?: return null

        val fajr = nextDayPrayers
            .map { it.get(Prayer.FAJR) }
            .getOrNull() ?: return null

        // The night in islam is the time between Maghrib and Fajr
        val nightTimeInMinutes = instantBetween(maghribDateTime, fajr.dateTime).inWholeMinutes

        return mapOf(
            // After middle of the night
            PrayerStatus.Late to run {
                val start = nightTimeInMinutes / 2

                maghribDateTime.plus(start, DateTimeUnit.MINUTE)..<fajr.dateTime
            },
            // Between first third of the night and middle of the night
            PrayerStatus.AfterHalfTime to run {
                val start = nightTimeInMinutes / 3
                val end = nightTimeInMinutes / 2

                val startingDateTime = maghribDateTime.plus(start, DateTimeUnit.MINUTE)
                val endingDateTime = maghribDateTime.plus(end, DateTimeUnit.MINUTE)

                startingDateTime..<endingDateTime
            },
            // Before first third of the night
            PrayerStatus.OnTime to run {
                val firstThirdOfNight = nightTimeInMinutes / 3

                prayerDateTime..<maghribDateTime.plus(firstThirdOfNight, DateTimeUnit.MINUTE)
            },
        )
    }

    companion object {
        private const val LATE_REMAINING_TIME_IN_MINUTES = 15
    }

}