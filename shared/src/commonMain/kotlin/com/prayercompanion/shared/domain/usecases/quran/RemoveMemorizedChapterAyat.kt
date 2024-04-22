package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.repositories.QuranRepository

class RemoveMemorizedChapterAyat constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(chapterId: Int): Result<Unit> {
        return quranRepository.deleteMemorizedChapterAyat(chapterId)
    }
}