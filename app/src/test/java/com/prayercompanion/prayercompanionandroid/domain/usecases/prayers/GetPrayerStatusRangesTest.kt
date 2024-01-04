package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.utils.FakeLocationRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersFakeRepository
import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.repositories.LocationRepository
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.usecases.prayers.GetPrayerStatusRanges
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test

internal class GetPrayerStatusRangesTest {

    private lateinit var usecase: GetPrayerStatusRanges
    private val prayersRepository: PrayersRepository = PrayersFakeRepository()
    private val locationRepository: LocationRepository = FakeLocationRepository()

    @Before
    fun setUp() {
        usecase = GetPrayerStatusRanges(prayersRepository, locationRepository)
    }

    @Test
    fun test() = runTest {
        val fajrPrayer = PrayersFakeRepository
            .getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.FAJR)
        val dhuhrPrayer = PrayersFakeRepository
            .getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.DHUHR)
        val asrPrayer = PrayersFakeRepository
            .getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.ASR)
        val maghribPrayer = PrayersFakeRepository
            .getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.MAGHRIB)
        val ishaPrayer = PrayersFakeRepository
            .getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.ISHA)

        val statuses = listOf(
            fajrPrayer to usecase.call(LocalDateTime.now(), Prayer.FAJR),
            dhuhrPrayer to usecase.call(LocalDateTime.now(), Prayer.DHUHR),
            asrPrayer to usecase.call(LocalDateTime.now(), Prayer.ASR),
            maghribPrayer to usecase.call(LocalDateTime.now(), Prayer.MAGHRIB),
            ishaPrayer to usecase.call(LocalDateTime.now(), Prayer.ISHA),
        )

        statuses.forEach { status ->
            println("${status.first.prayer}: ${status.first.time}")
            status.second?.entries?.reversed()?.forEach {
                println(
                    "   ${it.key}: ${it.value.start.time} .. ${it.value.endExclusive.time}"
                )
            }
        }
    }
}