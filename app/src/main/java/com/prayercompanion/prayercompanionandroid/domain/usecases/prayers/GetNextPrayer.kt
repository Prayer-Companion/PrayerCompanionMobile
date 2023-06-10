package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import java.time.LocalDate
import javax.inject.Inject

class GetNextPrayer @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager
) {

    suspend fun call(prayerInfo: PrayerInfo): Result<PrayerInfo> {
        val location = appLocationManager.getLastKnownLocation()
        val address = appLocationManager.getAddress()

        var nextPrayer = prayerInfo.prayer.next()
        var date: LocalDate = prayerInfo.date
        if (nextPrayer == null) {
            nextPrayer = Prayer.FAJR
            date = date.plusDays(1)
        }

        return prayersRepository.getPrayer(
            prayer = nextPrayer,
            date = date,
            location = location,
            address = address
        )
    }
}