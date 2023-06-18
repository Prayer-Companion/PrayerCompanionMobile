package com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran

sealed class QuranEvent {
    data class OnChapterSelected(
        val chapterId: Int,
        val fromVerse: Int,
        val toVerse: Int
    ) : QuranEvent()

    data class OnChapterAyatSaved(
        val chapterId: Int,
        val fromVerse: Int,
        val toVerse: Int
    ) : QuranEvent()

    data class OnChapterDeselected(val chapterId: Int) : QuranEvent()
    data class OnSearchQueryChanged(val query: String) : QuranEvent()
    object OnLoadQuranSectionsClicked : QuranEvent()
    object OnNextSectionClicked : QuranEvent()
    object OnViewFullClicked : QuranEvent()

}
