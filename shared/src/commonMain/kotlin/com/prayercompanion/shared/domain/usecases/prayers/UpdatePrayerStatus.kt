package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.repositories.PrayersRepository

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