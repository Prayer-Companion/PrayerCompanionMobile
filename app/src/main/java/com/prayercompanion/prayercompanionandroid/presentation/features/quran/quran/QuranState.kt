package com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter

class QuranState {
    var quranChapters by mutableStateOf(emptyList<QuranChapter>())
    var sections: PrayerQuranReadingSections? by mutableStateOf(null)
    var searchQuery: String by mutableStateOf("")
    val filteredQuranChapters: List<QuranChapter> get() = quranChapters.filter { searchQuery in it.name }

}
