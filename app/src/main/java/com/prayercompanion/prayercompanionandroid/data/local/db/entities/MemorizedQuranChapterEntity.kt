package com.prayercompanion.prayercompanionandroid.data.local.db.entities

data class MemorizedQuranChapterEntity(
    val id: Int = 0,
    val chapterId: Int,
    val memorizedFrom: Int,
    val memorizedTo: Int
)
