package com.prayercompanion.prayercompanionandroid.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranReadingSectionsDao {

    @Insert
    fun insertReadingSections(quranReadingSectionResponses: List<QuranReadingSectionEntity>)

    @Query("SELECT * FROM QuranReadingSections WHERE isRead = 0 ORDER BY id LIMIT 2")
    fun getNextReadingSections(): Flow<List<QuranReadingSectionEntity>>

    @Query("Update QuranReadingSections set isRead = 1 where id in (:ids)")
    fun markSectionAsRead(ids: List<Int>)

    @Query("DELETE FROM QuranReadingSections WHERE chapterId == :chapterId")
    fun deleteQuranReadingSectionByChapter(chapterId: Int)
}
