package com.prayercompanion.prayercompanionandroid.domain.usecases.quran


import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections

class MarkQuranSectionAsRead constructor(
    private val repo: QuranRepository
) {

    suspend fun call(sections: PrayerQuranReadingSections) {
        repo.markQuranSectionAsRead(sections)
    }
}