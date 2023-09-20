package com.prayercompanion.prayercompanionandroid.domain.usecases.quran

import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository

class LoadQuranReadingSuggestions constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Result<Unit> {
        return quranRepository.loadAndSaveQuranReadingSections()
    }
}