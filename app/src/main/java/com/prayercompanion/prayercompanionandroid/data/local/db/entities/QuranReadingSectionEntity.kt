package com.prayercompanion.prayercompanionandroid.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("QuranReadingSections")
data class QuranReadingSectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("chapterId")
    val chapterId: Int,
    @ColumnInfo("startVerse")
    val startVerse: Int,
    @ColumnInfo("endVerse")
    val endVerse: Int,
    @ColumnInfo("isRead")
    val isRead: Boolean
)