package com.prayercompanion.prayercompanionandroid.data.repositories

import com.prayercompanion.prayercompanionandroid.data.local.assets.AssetsReader
import com.prayercompanion.prayercompanionandroid.data.local.assets.mappers.toQuran
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity
import com.prayercompanion.prayercompanionandroid.data.local.db.mappers.toPrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.Quran
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val assetsReader: AssetsReader,
    private val api: PrayerCompanionApi,
    private val dao: QuranReadingSectionsDao
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

    override suspend fun getNextQuranReadingSections(): Result<PrayerQuranReadingSections> {
        val entities = dao.getNextReadingSections()
        return if (entities.size < 2) {
            loadAndSaveQuranReadingSections()
        } else {
            mapEntitiesToNextQuranReadingSections(entities)
        }
    }

    override suspend fun markQuranSectionAsRead(quranReadingSections: PrayerQuranReadingSections) {
        dao.markSectionAsRead(
            listOf(
                quranReadingSections.firstSection.sectionId,
                quranReadingSections.secondSection.sectionId
            )
        )
    }

    private suspend fun mapEntitiesToNextQuranReadingSections(entities: List<QuranReadingSectionEntity>): Result<PrayerQuranReadingSections> {
        return getFullQuran()
            .map { quran ->
                entities
                    .take(2)
                    .toPrayerQuranReadingSections(quran)
            }
    }

    private suspend fun loadAndSaveQuranReadingSections(): Result<PrayerQuranReadingSections> {
        return try {
            val entities = api.getQuranReadingSections()
                .map {
                    QuranReadingSectionEntity(
                        chapterId = it.chapterId,
                        startVerse = it.startVerse,
                        endVerse = it.endVerse,
                        isRead = false
                    )
                }
            dao.insertReadingSections(entities)
            val next = dao.getNextReadingSections()
            mapEntitiesToNextQuranReadingSections(next)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}