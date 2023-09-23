package com.prayercompanion.prayercompanionandroid.domain.models

import com.prayercompanion.prayercompanionandroid.domain.extensions.max
import com.prayercompanion.prayercompanionandroid.domain.extensions.min
import com.prayercompanion.prayercompanionandroid.domain.extensions.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus

data class DailyPrayersCombo(
    val todayPrayersInfo: DayPrayersInfo,
    val yesterdayPrayersInfo: DayPrayersInfo?,
    val tomorrowPrayersInfo: DayPrayersInfo?
) {

    val currentAndNextPrayer: Pair<PrayerInfo, PrayerInfo>
        get() {
            val currentPrayer = currentPrayer
            return currentPrayer to getNextPrayer(currentPrayer)
        }

    private val now get() = LocalTime.now()

    private val currentPrayer: PrayerInfo
        get() {
            val todayFajr = todayPrayersInfo.get(Prayer.FAJR)
            val todayIsha = todayPrayersInfo.get(Prayer.ISHA)

            if (now in LocalTime.min() ..< todayFajr.time) {
                return yesterdayPrayersInfo?.get(Prayer.ISHA) ?: PrayerInfo.Default
            }

            if (now in todayIsha.time .. LocalTime.max()) {
                return todayIsha
            }

            todayPrayersInfo.prayers.take(todayPrayersInfo.prayers.size - 1)
                .forEachIndexed { index, prayerInfo ->
                    val prayerTime = prayerInfo.time
                    val nextPrayerTime = todayPrayersInfo.prayers[index + 1].time
                    if (now in prayerTime ..< nextPrayerTime) {
                        return prayerInfo
                    }
                }

            throw Exception("Something went wrong while getting current prayer")
        }

    private fun getNextPrayer(prayerInfo: PrayerInfo): PrayerInfo {
        val nextPrayer = prayerInfo.prayer.next()

        return if (nextPrayer != null) {
            todayPrayersInfo.get(nextPrayer)
        } else {
            val today = LocalDate.now()
            val yesterday = today.minus(1, DateTimeUnit.DAY)

            // if it is today's Isha then get tomorrow's Fajr
            // if it is yesterday's Isha then get today's Fajr
            when (prayerInfo.date) {
                today -> tomorrowPrayersInfo?.get(Prayer.FAJR)
                yesterday -> todayPrayersInfo.get(Prayer.FAJR)
                else -> null
            } ?: PrayerInfo.Default
        }
    }
}
