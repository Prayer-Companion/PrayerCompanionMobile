package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFullQuranWithMemorized @Inject constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Flow<Result<List<QuranChapter>>> {
        return quranRepository.getFullQuranFlow()
    }
}