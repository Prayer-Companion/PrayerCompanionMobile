package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
class SetPrayerStatusByDateTime
@Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val getPrayerStatusRanges: GetPrayerStatusRanges
) {

    @ExperimentalStdlibApi
    suspend fun call(prayerInfo: PrayerInfo, dateTime: LocalDateTime): PrayerStatus {
        val ranges = getPrayerStatusRanges.call(prayerInfo)

        val status: PrayerStatus = ranges?.firstNotNullOfOrNull {
            if (dateTime in it.value) it.key else null
        } ?: PrayerStatus.Qadaa

        prayersRepository.updatePrayerStatus(prayerInfo, status)

        return status
    }

}