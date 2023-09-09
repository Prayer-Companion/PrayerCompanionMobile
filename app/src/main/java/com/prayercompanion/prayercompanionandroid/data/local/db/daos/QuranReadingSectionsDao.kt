package com.prayercompanion.prayercompanionandroid.data.local.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity
import com.prayercompanion.prayercompanionandroid.toBoolean
import com.prayercompanion.prayercompanionandroid.toLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface QuranReadingSectionsDao {
    fun insertReadingSections(entities: List<QuranReadingSectionEntity>)
    fun getNextReadingSections(): Flow<List<QuranReadingSectionEntity>>
    fun markSectionAsRead(ids: List<Int>)
    fun deleteQuranReadingSectionByChapter(chapterId: Int)
}


class QuranReadingSectionsDaoImpl @Inject constructor(
    db: PrayerCompanionDatabase
) : QuranReadingSectionsDao {

    private val queries = db.quranReadingSectionsQueries

    override fun insertReadingSections(entities: List<QuranReadingSectionEntity>) {
        queries.transaction {
            afterRollback { throw Exception("Failed to insert reading sections ") }
            entities.forEach {
                queries.insert(
                    id = null,
                    chapterId = it.chapterId.toLong(),
                    startVerse = it.startVerse.toLong(),
                    endVerse = it.endVerse.toLong(),
                    isRead = it.isRead.toLong()
                )
            }
        }
    }

    override fun getNextReadingSections(): Flow<List<QuranReadingSectionEntity>> {
        return queries.getNextReadingSections { id, chapterId, startVerse, endVerse, isRead ->
            QuranReadingSectionEntity(
                id = id.toInt(),
                chapterId = chapterId.toInt(),
                startVerse = startVerse.toInt(),
                endVerse = endVerse.toInt(),
                isRead = isRead.toBoolean()
            )
        }.asFlow().mapToList(Dispatchers.IO)
    }

    override fun markSectionAsRead(ids: List<Int>) {
        queries.markSectionAsRead(ids.map { it.toLong() })
    }

    override fun deleteQuranReadingSectionByChapter(chapterId: Int) {
        queries.deleteQuranReadingSectionByChapter(chapterId.toLong())
    }
}