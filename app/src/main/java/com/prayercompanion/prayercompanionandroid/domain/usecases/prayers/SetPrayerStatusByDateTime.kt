package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SetPrayerStatusByDateTime
@Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val getPrayerStatusRanges: GetPrayerStatusRanges
) {

    suspend fun call(prayerInfo: PrayerInfo, dateTime: LocalDateTime): Result<PrayerStatus> {
        val ranges = getPrayerStatusRanges.call(prayerInfo.dateTime, prayerInfo.prayer.next())

        val status: PrayerStatus = ranges?.firstNotNullOfOrNull {
            if (dateTime in it.value) it.key else null
        } ?: PrayerStatus.Qadaa

        return prayersRepository.updatePrayerStatus(prayerInfo, status)
            .map { status }
    }

}