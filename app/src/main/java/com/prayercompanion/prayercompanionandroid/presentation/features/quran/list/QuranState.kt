package com.prayercompanion.prayercompanionandroid.presentation.features.quran.list

import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.Quran
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter

data class QuranState(
    val quran: Quran = Quran(emptyList()),
    val sections: PrayerQuranReadingSections? = null,
    val searchQuery: String = ""
) {
    val filteredQuranChapters: List<QuranChapter> get() = quran.chapters.filter { searchQuery in it.name }

}
