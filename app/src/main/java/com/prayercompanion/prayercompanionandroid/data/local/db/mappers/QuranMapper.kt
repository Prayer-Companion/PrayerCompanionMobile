package com.prayercompanion.prayercompanionandroid.data.local.db.mappers

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.Quran

fun List<QuranReadingSectionEntity>.toPrayerQuranReadingSections(quran: Quran): PrayerQuranReadingSections {
    require(size == 2)

    val first = get(0)
    val firstChapter = quran.chapters
        .find { it.id == first.chapterId }
    val firstVerses = firstChapter
        ?.verses
        ?.slice(first.startVerse - 1 until first.endVerse)
    requireNotNull(firstChapter)
    requireNotNull(firstVerses)

    val second = get(1)
    val secondChapter = quran.chapters
        .find { it.id == second.chapterId }
    val secondVerses = secondChapter
        ?.verses
        ?.slice(second.startVerse - 1 until second.endVerse)

    requireNotNull(secondChapter)
    requireNotNull(secondVerses)

    return PrayerQuranReadingSections(
        PrayerQuranReadingSection(
            sectionId = first.id,
            chapterId = first.chapterId,
            chapterName = firstChapter.name,
            startVerse = first.startVerse,
            endVerse = first.endVerse,
            verses = firstVerses
        ),
        PrayerQuranReadingSection(
            sectionId = second.id,
            chapterId = second.chapterId,
            chapterName = secondChapter.name,
            startVerse = second.startVerse,
            endVerse = second.endVerse,
            verses = secondVerses
        )
    )
}