package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.repositories.QuranRepository

class LoadQuranReadingSuggestions constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Result<Unit> {
        return quranRepository.loadAndSaveQuranReadingSections()
    }
}