package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.extensions.minus
import com.prayercompanion.prayercompanionandroid.domain.extensions.now
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import java.util.SortedMap

class GetStatusesOverView constructor(
    private val repository: PrayersRepository
) {

    fun call(): Flow<SortedMap<PrayerStatus, Int>> {
        val now = LocalDateTime.now()
        val startDateTime = now.minus(3 * 24, DateTimeUnit.HOUR)

        return repository.getStatusesByDate(startDateTime, now)
            .map { it ->
                val statuses = it.mapNotNull { it }

                val order = listOf(
                    PrayerStatus.Jamaah,
                    PrayerStatus.OnTime,
                    PrayerStatus.AfterHalfTime,
                    PrayerStatus.Late,
                    PrayerStatus.Qadaa,
                    PrayerStatus.Missed
                )

                statuses
                    .groupingBy { it }
                    .eachCount()
                    .toSortedMap { t, t2 ->
                        order.indexOf(t) - order.indexOf(t2)
                    }
            }
    }
}