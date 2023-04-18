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
        date: LocalDate
    ): Result<DayPrayersInfo>

    suspend fun getPrayer(prayer: Prayer, date: LocalDate): PrayerInfo

    suspend fun updatePrayerStatus(
        date: LocalDate,
        prayer: PrayerInfo,
        status: PrayerStatus
    ): Result<PrayerStatus>
}