package com.prayercompanion.prayercompanionandroid.presentation.features.quran

sealed class QuranEvent {
    data class OnChapterSelected(val chapterId: Int, val fromVerse: Int, val toVerse: Int) : QuranEvent()
    data class OnChapterAyatSaved(val chapterId: Int, val fromVerse: Int, val toVerse: Int) : QuranEvent()
    data class OnChapterDeselected(val chapterId: Int) : QuranEvent()
}
