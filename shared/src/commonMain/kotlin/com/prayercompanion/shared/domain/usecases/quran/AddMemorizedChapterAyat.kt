package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.repositories.QuranRepository

class AddMemorizedChapterAyat constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(chapterId: Int, startVerse: Int, endVerse: Int): Result<Unit> {
        return quranRepository.addMemorizedChapterAyat(chapterId, startVerse, endVerse)
    }
}