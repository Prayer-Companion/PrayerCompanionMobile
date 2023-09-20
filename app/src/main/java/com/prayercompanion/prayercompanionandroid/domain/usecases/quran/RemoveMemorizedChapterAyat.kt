package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository

class RemoveMemorizedChapterAyat constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(chapterId: Int): Result<Unit> {
        return quranRepository.deleteMemorizedChapterAyat(chapterId)
    }
}