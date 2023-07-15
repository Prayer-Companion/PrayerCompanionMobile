package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import javax.inject.Inject

class LoadAndSaveQuranMemorizedChapters @Inject constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call() {
        quranRepository.loadAndSaveMemorizedChapters()
    }
}