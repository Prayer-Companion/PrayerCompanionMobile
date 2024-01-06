package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import com.raedghazal.kotlinx_datetime_ext.minus
import com.raedghazal.kotlinx_datetime_ext.now
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime

class GetStatusesOverView constructor(
    private val repository: PrayersRepository
) {

    fun call(): Flow<List<Pair<PrayerStatus, Int>>> {
        val now = LocalDateTime.now()
        val startDateTime = now.minus(3 * 24, DateTimeUnit.HOUR)

        return repository.getStatusesByDate(startDateTime, now)
            .map { it ->
                val statuses = it.mapNotNull { it }

                val order = PrayerStatus.values().toList()

                statuses
                    .groupingBy { it }
                    .eachCount()
                    .filter { it.key != PrayerStatus.None }
                    .map {
                        it.key to it.value
                    }.sortedWith { t1, t2 ->
                        order.indexOf(t1.first) - order.indexOf(t2.first)
                    }
            }
    }
}