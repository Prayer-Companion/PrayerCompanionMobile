package com.prayercompanion.prayercompanionandroid.domain.models.quran

data class PrayerQuranReadingSection(
    val sectionId: Int,
    val chapterId: Int,
    val chapterName: String,
    val startVerse: Int,
    val endVerse: Int,
    val verses: List<QuranVerse>
)
