package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.PrayerStatusWithTimeRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class GetDayPrayersFlow constructor(
    private val prayersRepository: PrayersRepository,
    private val appLocationManager: AppLocationManager,
    private val getPrayerStatusRanges: GetPrayerStatusRanges
) {

    suspend fun call(
        date: LocalDate
    ): Flow<Result<DayPrayersInfo>> {
        val location = appLocationManager.getLastKnownLocation()
        val address = appLocationManager.getAddressByLocation(location)

        val flow = prayersRepository.getDayPrayersFlow(location, address, date)

        val result = flow.map { dayPrayersInfoResult ->
            dayPrayersInfoResult.map { entities ->
                val prayers = entities.map { prayerInfoEntity ->
                    mapToPrayerInfoWithStatusesRanges(prayerInfoEntity)
                }

                DayPrayersInfo(prayers)
            }
        }

        return result
    }

    private suspend fun mapToPrayerInfoWithStatusesRanges(prayerInfoEntity: PrayerInfoEntity): PrayerInfo {
        val ranges = getPrayerStatusRanges.call(
            prayerDateTime = prayerInfoEntity.dateTime,
            nextPrayer = prayerInfoEntity.prayer.next()
        ) ?: emptyMap()

        val statusesWithTimeRanges = PrayerStatus.values().map {
            PrayerStatusWithTimeRange(
                prayerStatus = it,
                range = ranges[it],
                prayer = prayerInfoEntity.prayer
            )
        }

        return PrayerInfo(
            prayer = prayerInfoEntity.prayer,
            dateTime = prayerInfoEntity.dateTime,
            selectedStatus = prayerInfoEntity.status,
            statusesWithTimeRanges = statusesWithTimeRanges
        )
    }

}