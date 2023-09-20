package com.prayercompanion.prayercompanionandroid.domain.usecases.quran


import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetNextQuranReadingSections constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Flow<PrayerQuranReadingSections?> {
        return quranRepository.getNextQuranReadingSections()
    }
}