package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.models.PrayerStatus

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