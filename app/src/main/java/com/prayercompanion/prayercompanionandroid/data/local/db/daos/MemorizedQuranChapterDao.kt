package com.prayercompanion.prayercompanionandroid.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.MemorizedQuranChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemorizedQuranChapterDao {

    @Insert
    fun insert(chapter: List<MemorizedQuranChapterEntity>)

    @Insert
    fun insert(chapter: MemorizedQuranChapterEntity)

    @Query("Delete from MemorizedQuranChapter")
    fun deleteAll()

    @Query("Delete from MemorizedQuranChapter where chapterId = :chapterId")
    fun delete(chapterId: Int)

    @Query("Update MemorizedQuranChapter set memorizedFrom=:startVerse, memorizedTo = :endVerse where chapterId = :chapterId")
    fun update(chapterId: Int, startVerse: Int, endVerse: Int)

    @Query("Select * from MemorizedQuranChapter")
    fun getAllMemorizedChaptersFlow(): Flow<List<MemorizedQuranChapterEntity>>

    @Transaction
    fun deleteAllAndInsertNew(chapters: List<MemorizedQuranChapterEntity>) {
        deleteAll()
        insert(chapters)
    }
}