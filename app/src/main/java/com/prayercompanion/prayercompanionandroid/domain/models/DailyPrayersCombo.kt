package com.prayercompanion.prayercompanionandroid.domain.models

import java.time.LocalDate
import java.time.LocalTime

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

    @OptIn(ExperimentalStdlibApi::class)
    private val currentPrayer: PrayerInfo
        get() {
            val todayFajr = todayPrayersInfo.get(Prayer.FAJR)
            val todayIsha = todayPrayersInfo.get(Prayer.ISHA)

            if (now in LocalTime.MIN.rangeUntil(todayFajr.time)) {
                return yesterdayPrayersInfo?.get(Prayer.ISHA) ?: PrayerInfo.Default
            }

            if (now in todayIsha.time.rangeTo(LocalTime.MAX)) {
                return todayIsha
            }

            todayPrayersInfo.prayers.take(todayPrayersInfo.prayers.size - 1)
                .forEachIndexed { index, prayerInfo ->
                    val prayerTime = prayerInfo.time
                    val nextPrayerTime = todayPrayersInfo.prayers[index + 1].time
                    if (now in prayerTime.rangeUntil(nextPrayerTime)) {
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
            val yesterday = today.minusDays(1)

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
