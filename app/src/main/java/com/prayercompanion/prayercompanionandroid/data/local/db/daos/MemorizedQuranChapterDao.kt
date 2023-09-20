package com.prayercompanion.prayercompanionandroid.data.local.db.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.MemorizedQuranChapterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface MemorizedQuranChapterDao {
    fun insert(chapters: List<MemorizedQuranChapterEntity>)
    fun insert(chapter: MemorizedQuranChapterEntity)
    fun deleteAll()
    fun delete(chapterId: Int)
    fun update(chapterId: Int, startVerse: Int, endVerse: Int)
    fun getAllMemorizedChaptersFlow(): Flow<List<MemorizedQuranChapterEntity>>
    fun deleteAllAndInsertNew(chapters: List<MemorizedQuranChapterEntity>) {
        deleteAll()
        insert(chapters)
    }
}

class MemorizedQuranChapterDaoImpl constructor(
    db: PrayerCompanionDatabase
) : MemorizedQuranChapterDao {

    private val queries = db.memorizedQuranChapterQueries

    override fun insert(chapters: List<MemorizedQuranChapterEntity>) {
        queries.transaction {
            afterRollback { throw Exception("Failed to insert memorized chapters ") }
            chapters.forEach {
                queries.insert(
                    id = null,
                    chapterId = it.chapterId.toLong(),
                    memorizedFrom = it.memorizedFrom.toLong(),
                    memorizedTo = it.memorizedTo.toLong()
                )
            }
        }
    }

    override fun insert(chapter: MemorizedQuranChapterEntity) {
        queries.insert(
            id = null,
            chapterId = chapter.chapterId.toLong(),
            memorizedFrom = chapter.memorizedFrom.toLong(),
            memorizedTo = chapter.memorizedTo.toLong()
        )
    }

    override fun deleteAll() {
        queries.deleteAll()
    }

    override fun delete(chapterId: Int) {
        queries.delete(chapterId.toLong())
    }

    override fun update(chapterId: Int, startVerse: Int, endVerse: Int) {
        queries.update(chapterId.toLong(), startVerse.toLong(), endVerse.toLong())
    }

    override fun getAllMemorizedChaptersFlow(): Flow<List<MemorizedQuranChapterEntity>> {
        return queries.getAllMemorizedChapters { id, chapterId, memorizedFrom, memorizedTo ->
            MemorizedQuranChapterEntity(
                id = id.toInt(),
                chapterId = chapterId.toInt(),
                memorizedFrom = memorizedFrom.toInt(),
                memorizedTo = memorizedTo.toInt()
            )
        }.asFlow().mapToList(Dispatchers.IO)
    }
}