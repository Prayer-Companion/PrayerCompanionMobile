package com.prayercompanion.prayercompanionandroid.data.repositories

import com.prayercompanion.prayercompanionandroid.data.local.assets.AssetsReader
import com.prayercompanion.prayercompanionandroid.data.local.assets.mappers.toQuran
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.domain.models.Quran
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val assetsReader: AssetsReader,
    private val api: PrayerCompanionApi
) : QuranRepository {

    override suspend fun getFullQuran(): Result<Quran> {
        var quran = assetsReader.quran.map { it.toQuran() }
        kotlin.runCatching {
            val memorizedChapterVerses = api.getMemorizedChapterVerses()

            quran = quran.map {
                memorizedChapterVerses.forEach { memorized ->
                    it.chapters.find { chapter ->
                        chapter.id == memorized.chapterId
                    }?.apply {
                        isMemorized = true
                        memorizedFrom = memorized.startVerse
                        memorizedTo = memorized.endVerse
                    }
                }
                it
            }
        }
        return quran
    }

    override suspend fun addMemorizedChapterAyat(
        chapterId: Int,
        startVerse: Int,
        endVerse: Int
    ): Result<Unit> {
        return try {
            api.addOrUpdateMemorizedChapterVerses(chapterId, startVerse, endVerse)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMemorizedChapterAyat(
        chapterId: Int,
        startVerse: Int,
        endVerse: Int
    ): Result<Unit> {
        return try {
            api.addOrUpdateMemorizedChapterVerses(chapterId, startVerse, endVerse)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMemorizedChapterAyat(chapterId: Int): Result<Unit> {
        return try {
            api.deleteMemorizedChapterVerses(chapterId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}