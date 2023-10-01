package com.prayercompanion.prayercompanionandroid.presentation.features.quran.full_sections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.domain.usecases.quran.GetNextQuranReadingSections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullPrayerQuranSectionsViewModel(
    private val getNextQuranReadingSections: GetNextQuranReadingSections
) : ViewModel() {
    var quranReadingSections: PrayerQuranReadingSections by mutableStateOf(
        PrayerQuranReadingSections.EMPTY
    )
        private set

    init {
        viewModelScope.launch {
            getNextQuranReadingSections.call().collectLatest { sections ->
                withContext(Dispatchers.Main) {
                    if (sections != null) {
                        quranReadingSections = sections
                    }
                }
            }
        }
    }
}