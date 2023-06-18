package com.prayercompanion.prayercompanionandroid.domain.usecases.quran


import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNextQuranReadingSections @Inject constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Flow<PrayerQuranReadingSections?> {
        return quranRepository.getNextQuranReadingSections()
    }
}