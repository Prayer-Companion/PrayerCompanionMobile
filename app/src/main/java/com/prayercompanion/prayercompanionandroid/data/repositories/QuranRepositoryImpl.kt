package com.prayercompanion.prayercompanionandroid.data.repositories

import com.prayercompanion.prayercompanionandroid.data.local.assets.AssetsReader
import com.prayercompanion.prayercompanionandroid.data.local.assets.mappers.toQuranChapters
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.MemorizedQuranChapterDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.MemorizedQuranChapterEntity
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import logcat.asLog
import logcat.logcat
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    assetsReader: AssetsReader,
    private val api: PrayerCompanionApi,
    private val readingSectionDao: QuranReadingSectionsDao,
    private val memorizedChaptersDao: MemorizedQuranChapterDao
) : QuranRepository {

    private val quranChapters = assetsReader.quran.map { it.toQuranChapters() }

    override suspend fun loadAndSaveMemorizedChapters() {
        try {
            val chapters = api.getMemorizedChapterVerses()
            val entities = chapters.map {
                MemorizedQuranChapterEntity(
                    chapterId = it.chapterId,
                    memorizedFrom = it.startVerse,
                    memorizedTo = it.endVerse
                )
            }
            memorizedChaptersDao.deleteAllAndInsertNew(entities)
        } catch (e: Exception) {
            logcat { e.asLog() }
        }
    }

    override suspend fun getFullQuranFlow(): Flow<Result<List<QuranChapter>>> {
        val quranChapters = this.quranChapters

        kotlin.runCatching {
            val memorizedChapterVerses = memorizedChaptersDao.getAllMemorizedChaptersFlow()

            return memorizedChapterVerses.map { memorizedChapters ->
                quranChapters.map { chapters ->
                    val mutableChapters = chapters.toMutableList()

                    memorizedChapters.forEach { memorized ->
                        val index = mutableChapters.indexOfFirst { it.id == memorized.chapterId }
                            .takeUnless { it == -1 } ?: return@forEach
                        mutableChapters[index] = mutableChapters[index].copy(
                            isMemorized = true,
                            memorizedFrom = memorized.memorizedFrom,
                            memorizedTo = memorized.memorizedTo
                        )
                    }

                    mutableChapters
                }
            }
        }

        return flowOf(quranChapters)
    }

    override suspend fun addMemorizedChapterAyat(
        chapterId: Int,
        startVerse: Int,
        endVerse: Int
    ): Result<Unit> {
        return try {
            api.addOrUpdateMemorizedChapterVerses(chapterId, startVerse, endVerse)
            memorizedChaptersDao.insert(
                MemorizedQuranChapterEntity(
                    chapterId = chapterId,
                    memorizedFrom = startVerse,
                    memorizedTo = endVerse
                )
            )
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
            memorizedChaptersDao.update(chapterId, startVerse, endVerse)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMemorizedChapterAyat(chapterId: Int): Result<Unit> {
        return try {
            api.deleteMemorizedChapterVerses(chapterId)
            readingSectionDao.deleteQuranReadingSectionByChapter(chapterId)
            memorizedChaptersDao.delete(chapterId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNextQuranReadingSections(): Flow<PrayerQuranReadingSections?> {
        val entities = readingSectionDao.getNextReadingSections()
        return entities.map {
            mapEntitiesToNextQuranReadingSections(it).getOrNull()
        }
    }

    override suspend fun markQuranSectionAsRead(quranReadingSections: PrayerQuranReadingSections) {
        readingSectionDao.markSectionAsRead(
            listOf(
                quranReadingSections.firstSection.sectionId,
                quranReadingSections.secondSection.sectionId
            )
        )
        if (readingSectionDao.getNextReadingSections().first().isEmpty()) {
            loadAndSaveQuranReadingSections()
        }
    }

    override suspend fun loadAndSaveQuranReadingSections(): Result<Unit> {
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
            if (entities.isNotEmpty()) {
                readingSectionDao.insertReadingSections(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapEntitiesToNextQuranReadingSections(entities: List<QuranReadingSectionEntity>): Result<PrayerQuranReadingSections?> {
        if (entities.isEmpty()) {
            return Result.success(null)
        }

        val mutableEntities = entities.toMutableList()

        if (mutableEntities.size == 1) {
            mutableEntities.add(mutableEntities.first())
        } else {
            mutableEntities.take(2)
        }

        return quranEntitiesToPrayerQuranReadingSections(mutableEntities)
    }

    private fun quranEntitiesToPrayerQuranReadingSections(
        entities: List<QuranReadingSectionEntity>
    ): Result<PrayerQuranReadingSections> {
        return quranChapters
            .map { quranChapters ->
                require(entities.size == 2)

                val first = entities[0]
                val firstChapter = quranChapters
                    .find { it.id == first.chapterId }
                val firstVerses = firstChapter
                    ?.verses
                    ?.slice(first.startVerse - 1 until first.endVerse)
                requireNotNull(firstChapter)
                requireNotNull(firstVerses)

                val second = entities[1]
                val secondChapter = quranChapters
                    .find { it.id == second.chapterId }
                val secondVerses = secondChapter
                    ?.verses
                    ?.slice(second.startVerse - 1 until second.endVerse)

                requireNotNull(secondChapter)
                requireNotNull(secondVerses)

                PrayerQuranReadingSections(
                    PrayerQuranReadingSection(
                        sectionId = first.id,
                        chapterId = first.chapterId,
                        chapterName = firstChapter.name,
                        startVerse = first.startVerse,
                        endVerse = first.endVerse,
                        verses = firstVerses
                    ),
                    PrayerQuranReadingSection(
                        sectionId = second.id,
                        chapterId = second.chapterId,
                        chapterName = secondChapter.name,
                        startVerse = second.startVerse,
                        endVerse = second.endVerse,
                        verses = secondVerses
                    )
                )
            }
    }

}