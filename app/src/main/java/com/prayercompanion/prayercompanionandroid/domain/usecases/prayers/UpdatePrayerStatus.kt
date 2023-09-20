package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository

class UpdatePrayerStatus constructor(
    private val prayersRepository: PrayersRepository
) {

    suspend fun call(
        prayerInfo: PrayerInfo,
        prayerStatus: PrayerStatus
    ): Result<Unit> {
        return prayersRepository.updatePrayerStatus(prayerInfo, prayerStatus)
    }
}