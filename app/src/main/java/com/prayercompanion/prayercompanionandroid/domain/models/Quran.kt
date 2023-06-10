package com.prayercompanion.prayercompanionandroid.domain.models

data class Quran(
    val chapters: List<QuranChapter>
)

data class QuranChapter(
    val id: Int,
    val name: String,
    val verses: List<QuranVerse>,
    var isMemorized: Boolean,
    var memorizedFrom: Int,
    var memorizedTo: Int
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

data class QuranVerse(
    val index: Int,
    val text: String,
    val hasBismillah: Boolean
)
