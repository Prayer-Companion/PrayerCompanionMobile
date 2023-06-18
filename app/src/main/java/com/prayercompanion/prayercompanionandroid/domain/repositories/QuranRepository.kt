package com.prayercompanion.prayercompanionandroid.domain.repositories

import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    suspend fun getFullQuran(): Result<List<QuranChapter>>
    suspend fun addMemorizedChapterAyat(
        chapterId: Int,
        startVerse: Int,
        endVerse: Int
    ): Result<Unit>

    suspend fun updateMemorizedChapterAyat(
        chapterId: Int,
        startVerse: Int,
        endVerse: Int
    ): Result<Unit>

    suspend fun deleteMemorizedChapterAyat(chapterId: Int): Result<Unit>
    suspend fun getNextQuranReadingSections(): Flow<PrayerQuranReadingSections?>
    suspend fun markQuranSectionAsRead(quranReadingSections: PrayerQuranReadingSections)
    suspend fun loadAndSaveQuranReadingSections(): Result<Unit>
}