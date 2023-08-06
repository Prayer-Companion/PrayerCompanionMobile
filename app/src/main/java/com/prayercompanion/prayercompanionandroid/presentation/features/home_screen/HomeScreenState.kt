package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import com.prayercompanion.prayercompanionandroid.domain.models.DailyPrayersCombo
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDate
import java.util.SortedMap

data class HomeScreenState(
    /** this parameter is only used to identify current and next prayer,
     * * *the status is not modified when its updated from in the HomeScreen*
     */
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
}