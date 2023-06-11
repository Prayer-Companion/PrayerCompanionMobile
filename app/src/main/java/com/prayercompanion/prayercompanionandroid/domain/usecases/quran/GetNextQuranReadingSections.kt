package com.prayercompanion.prayercompanionandroid.domain.usecases.quran


import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import javax.inject.Inject

class GetNextQuranReadingSections @Inject constructor(
    private val quranRepository: QuranRepository
) {

    suspend fun call(): Result<PrayerQuranReadingSections> {
        return quranRepository.getNextQuranReadingSections()
    }
}