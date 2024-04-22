package com.prayercompanion.shared.presentation.features.main.quran.full_sections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.domain.usecases.quran.GetNextQuranReadingSections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullPrayerQuranSectionsViewModel(
    private val getNextQuranReadingSections: GetNextQuranReadingSections
) : ScreenModel {
    var quranReadingSections: PrayerQuranReadingSections by mutableStateOf(
        PrayerQuranReadingSections.EMPTY
    )
        private set

    init {
        screenModelScope.launch {
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