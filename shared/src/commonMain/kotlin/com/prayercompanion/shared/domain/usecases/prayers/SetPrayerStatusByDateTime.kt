package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import kotlinx.datetime.LocalDateTime

class SetPrayerStatusByDateTime
constructor(
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