package com.prayercompanion.shared.domain.usecases.quran

import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.domain.repositories.QuranRepository

class MarkQuranSectionAsRead constructor(
    private val repo: QuranRepository
) {

    suspend fun call(sections: PrayerQuranReadingSections) {
        repo.markQuranSectionAsRead(sections)
    }
}