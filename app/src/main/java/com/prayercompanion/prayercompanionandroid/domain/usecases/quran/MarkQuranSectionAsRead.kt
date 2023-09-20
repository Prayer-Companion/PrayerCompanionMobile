package com.prayercompanion.prayercompanionandroid.domain.usecases.quran


import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository

class MarkQuranSectionAsRead constructor(
    private val repo: QuranRepository
) {

    suspend fun call(sections: PrayerQuranReadingSections) {
        repo.markQuranSectionAsRead(sections)
    }
}