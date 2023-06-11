package com.prayercompanion.prayercompanionandroid.domain.models.quran

data class PrayerQuranReadingSections(
    val firstSection: PrayerQuranReadingSection,
    val secondSection: PrayerQuranReadingSection,
) {
    companion object {
        val EMPTY = PrayerQuranReadingSections(
            firstSection = PrayerQuranReadingSection(
                sectionId = 0,
                chapterId = 0,
                chapterName = "",
                startVerse = 0,
                endVerse = 0,
                verses = listOf()
            ), secondSection = PrayerQuranReadingSection(
                sectionId = 0,
                chapterId = 0,
                chapterName = "",
                startVerse = 0,
                endVerse = 0,
                verses = listOf()
            )
        )
    }
}
