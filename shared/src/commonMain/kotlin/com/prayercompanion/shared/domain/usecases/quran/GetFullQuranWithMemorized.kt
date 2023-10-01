package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.models.quran.QuranChapter
import com.prayercompanion.shared.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetFullQuranWithMemorized constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Flow<Result<List<QuranChapter>>> {
        return quranRepository.getFullQuranFlow()
    }
}