package com.prayercompanion.prayercompanionandroid.domain.repositories

import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.Quran

interface QuranRepository {
    suspend fun getFullQuran(): Result<Quran>
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
    suspend fun getNextQuranReadingSections(): Result<PrayerQuranReadingSections>
    suspend fun markQuranSectionAsRead(quranReadingSections: PrayerQuranReadingSections)
}