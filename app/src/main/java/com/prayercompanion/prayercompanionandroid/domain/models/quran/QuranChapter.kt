package com.prayercompanion.prayercompanionandroid.domain.models.quran

data class QuranChapter(
    val id: Int,
    val name: String,
    val verses: List<QuranVerse>,
    val isMemorized: Boolean,
    val memorizedFrom: Int,
    val memorizedTo: Int
) {
    val versesCount = verses.size

    constructor(
        index: Int,
        name: String,
        verses: List<QuranVerse>
    ) : this(
        id = index,
        name = name,
        verses = verses,
        isMemorized = false,
        memorizedFrom = 1,
        memorizedTo = verses.size,
    )
}
