package com.prayercompanion.shared.presentation.features.main.home_screen

import com.prayercompanion.shared.domain.models.DailyPrayersCombo
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.raedghazal.kotlinx_datetime_ext.now
import kotlinx.datetime.LocalDate

data class HomeScreenState(
    val selectedDayPrayersInfo: DayPrayersInfo = DayPrayersInfo.Default,
    val selectedDate: LocalDate = LocalDate.now(),
)

data class HomeScreenHeaderState(
    val todayDate: LocalDate = LocalDate.now(),
    /** this parameter is only used to identify current and next prayer,
     * * *the status is not modified when its updated from in the HomeScreen*
     */
    val dailyPrayersCombo: DailyPrayersCombo = DailyPrayersCombo(
        DayPrayersInfo.Default,
        DayPrayersInfo.Default,
        DayPrayersInfo.Default
    ),
    val statusesOverview: List<Pair<PrayerStatus, Int>> = listOf()
) {
    val currentAndNextPrayer get() = dailyPrayersCombo.currentAndNextPrayer
}