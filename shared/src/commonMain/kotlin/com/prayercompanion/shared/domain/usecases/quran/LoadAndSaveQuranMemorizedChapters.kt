package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.repositories.QuranRepository

class LoadAndSaveQuranMemorizedChapters constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call() {
        quranRepository.loadAndSaveMemorizedChapters()
    }
}