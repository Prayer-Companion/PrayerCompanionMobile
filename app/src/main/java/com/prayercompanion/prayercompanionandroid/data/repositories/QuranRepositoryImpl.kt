package com.prayercompanion.prayercompanionandroid.data.repositories

import com.prayercompanion.prayercompanionandroid.data.local.assets.AssetsReader
import com.prayercompanion.prayercompanionandroid.data.local.assets.mappers.toQuranChapters
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val assetsReader: AssetsReader,
    private val api: PrayerCompanionApi,
    private val dao: QuranReadingSectionsDao
) : QuranRepository {

    override suspend fun getFullQuran(): Result<List<QuranChapter>> {
        var quranChapters = assetsReader.quran.map { it.toQuranChapters() }

        kotlin.runCatching {
            val memorizedChapterVerses = api.getMemorizedChapterVerses()

            quranChapters = quranChapters.map { chapters ->
                val mutableChapters = chapters.toMutableList()
                memorizedChapterVerses.forEach { memorized ->
                    val index = mutableChapters.indexOfFirst { it.id == memorized.chapterId }
                    mutableChapters[index] = mutableChapters[index].copy(
                        isMemorized = true,
                        memorizedFrom = memorized.startVerse,
                        memorizedTo = memorized.endVerse
                    )
                }
                mutableChapters
            }
        }
        return quranChapters
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
            dao.deleteQuranReadingSectionByChapter(chapterId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNextQuranReadingSections(): Flow<PrayerQuranReadingSections?> {
        val entities = dao.getNextReadingSections()
        return entities.map {
            mapEntitiesToNextQuranReadingSections(it).getOrNull()
        }
    }

    override suspend fun markQuranSectionAsRead(quranReadingSections: PrayerQuranReadingSections) {
        dao.markSectionAsRead(
            listOf(
                quranReadingSections.firstSection.sectionId,
                quranReadingSections.secondSection.sectionId
            )
        )
        if (dao.getNextReadingSections().first().isEmpty()) {
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
                dao.insertReadingSections(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun mapEntitiesToNextQuranReadingSections(entities: List<QuranReadingSectionEntity>): Result<PrayerQuranReadingSections?> {
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

    private suspend fun quranEntitiesToPrayerQuranReadingSections(
        entities: List<QuranReadingSectionEntity>
    ): Result<PrayerQuranReadingSections> {
        return getFullQuran()
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