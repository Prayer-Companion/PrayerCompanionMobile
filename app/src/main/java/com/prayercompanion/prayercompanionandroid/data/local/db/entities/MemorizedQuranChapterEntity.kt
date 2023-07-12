package com.prayercompanion.prayercompanionandroid.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("MemorizedQuranChapter")
data class MemorizedQuranChapterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "chapterId")
    val chapterId: Int,
    @ColumnInfo(name = "memorizedFrom")
    val memorizedFrom: Int,
    @ColumnInfo(name = "memorizedTo")
    val memorizedTo: Int
)
