package com.prayercompanion.prayercompanionandroid.presentation.features.quran

import com.prayercompanion.prayercompanionandroid.domain.models.Quran

data class QuranState(
    val quran: Quran = Quran(emptyList())
)
