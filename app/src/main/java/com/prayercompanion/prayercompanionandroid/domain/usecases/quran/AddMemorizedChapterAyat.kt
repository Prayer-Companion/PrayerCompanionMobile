package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import javax.inject.Inject

class AddMemorizedChapterAyat @Inject constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(chapterId: Int, startVerse: Int, endVerse: Int): Result<Unit> {
        return quranRepository.addMemorizedChapterAyat(chapterId, startVerse, endVerse)
    }
}