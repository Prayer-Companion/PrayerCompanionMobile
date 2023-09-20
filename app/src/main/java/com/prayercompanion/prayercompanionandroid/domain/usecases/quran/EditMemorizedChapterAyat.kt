package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository

class EditMemorizedChapterAyat constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(chapterId: Int, startVerse: Int, endVerse: Int): Result<Unit> {
        return quranRepository.updateMemorizedChapterAyat(chapterId, startVerse, endVerse)
    }
}
