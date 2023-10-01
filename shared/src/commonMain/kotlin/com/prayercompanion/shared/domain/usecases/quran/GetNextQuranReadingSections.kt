package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetNextQuranReadingSections constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Flow<PrayerQuranReadingSections?> {
        return quranRepository.getNextQuranReadingSections()
    }
}