package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import com.prayercompanion.prayercompanionandroid.domain.models.DailyPrayersCombo
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDate
import java.util.SortedMap

data class HomeScreenState(
    val dailyPrayersCombo: DailyPrayersCombo = DailyPrayersCombo(
        DayPrayersInfo.Default,
        DayPrayersInfo.Default,
        DayPrayersInfo.Default
    ),
    val selectedDayPrayersInfo: DayPrayersInfo = DayPrayersInfo.Default,
    val selectedDate: LocalDate = LocalDate.now(),
    val lastWeekStatuses: SortedMap<PrayerStatus, Int> = sortedMapOf()
) {
    val currentAndNextPrayer get() = dailyPrayersCombo.currentAndNextPrayer

    fun updateStatus(prayerInfo: PrayerInfo, prayerStatus: PrayerStatus): HomeScreenState {
        val currentPrayer = currentAndNextPrayer.first
        val nextPrayer = currentAndNextPrayer.second

        if (prayerInfo.date == selectedDate) {
            selectedDayPrayersInfo.updateStatus(prayerInfo.prayer, prayerStatus)
        }
        if (currentPrayer.date == prayerInfo.date && currentPrayer.prayer == prayerInfo.prayer) {
            currentPrayer.status = prayerStatus
        }
        if (nextPrayer.date == prayerInfo.date && nextPrayer.prayer == prayerInfo.prayer) {
            nextPrayer.status = prayerStatus
        }
        return this
    }

}