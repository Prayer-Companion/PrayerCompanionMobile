package com.prayercompanion.prayercompanionandroid.domain.repositories

import android.location.Location
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDate

interface PrayersRepository {

    suspend fun getDayPrayers(
        location: Location,
        dayDate: LocalDate,
        forceUpdate: Boolean = false
    ): Result<DayPrayersInfo>

    suspend fun getPrayer(prayer: Prayer, date: LocalDate): Result<PrayerInfo>

    suspend fun updatePrayerStatus(
        prayerInfo: PrayerInfo,
        prayerStatus: PrayerStatus
    ): Result<Unit>
}