package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository

class LoadAndSaveQuranMemorizedChapters constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call() {
        quranRepository.loadAndSaveMemorizedChapters()
    }
}