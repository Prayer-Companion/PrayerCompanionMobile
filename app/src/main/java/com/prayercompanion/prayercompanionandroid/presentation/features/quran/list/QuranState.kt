package com.prayercompanion.prayercompanionandroid.presentation.features.quran.list

import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.models.quran.Quran

data class QuranState(
    val quran: Quran = Quran(emptyList()),
    val sections: PrayerQuranReadingSections? = null
)
