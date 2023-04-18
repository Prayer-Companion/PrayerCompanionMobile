package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdatePrayerStatus @Inject constructor(
    private val prayersRepository: PrayersRepository
) {

    suspend fun call(date: LocalDate, prayerInfo: PrayerInfo, prayerStatus: PrayerStatus): Result<PrayerStatus> {
        return prayersRepository.updatePrayerStatus(date,prayerInfo,prayerStatus)
    }
}