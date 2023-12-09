package com.prayercompanion.shared.presentation.features.main.quran.quran

sealed class QuranEvent {
    data object OnStart: QuranEvent()
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
    data object OnLoadQuranSectionsClicked : QuranEvent()
    data object OnNextSectionClicked : QuranEvent()
    data object OnViewFullClicked : QuranEvent()

}
