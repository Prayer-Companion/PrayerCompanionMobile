package com.prayercompanion.prayercompanionandroid.data.local.db.entities


data class QuranReadingSectionEntity(
    val id: Int = 0,
    val chapterId: Int,
    val startVerse: Int,
    val endVerse: Int,
    val isRead: Boolean
)