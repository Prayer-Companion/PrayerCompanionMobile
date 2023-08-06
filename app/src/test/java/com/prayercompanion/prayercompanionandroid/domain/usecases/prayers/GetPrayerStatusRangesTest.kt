package com.prayercompanion.prayercompanionandroid.domain.usecases.prayers

import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.FakeAppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersFakeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

internal class GetPrayerStatusRangesTest {

    private lateinit var usecase: GetPrayerStatusRanges
    private val prayersRepository: PrayersRepository = PrayersFakeRepository()
    private val appLocationManager: AppLocationManager = FakeAppLocationManager()

    @Before
    fun setUp() {
        usecase = GetPrayerStatusRanges(prayersRepository, appLocationManager)
    }

    @Test
    fun test() = runTest {
        val fajrPrayer = PrayersFakeRepository.getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.FAJR)
        val dhuhrPrayer = PrayersFakeRepository.getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.DHUHR)
        val asrPrayer = PrayersFakeRepository.getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.ASR)
        val maghribPrayer = PrayersFakeRepository.getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.MAGHRIB)
        val ishaPrayer = PrayersFakeRepository.getFakeDayPrayersInfo(LocalDate.now()).get(Prayer.ISHA)

        val statuses = listOf(
            fajrPrayer to usecase.call(fajrPrayer),
            dhuhrPrayer to usecase.call(dhuhrPrayer),
            asrPrayer to usecase.call(asrPrayer),
            maghribPrayer to usecase.call(maghribPrayer),
            ishaPrayer to usecase.call(ishaPrayer),
        )

        statuses.forEach { status ->
            println("${status.first.prayer}: ${status.first.time}")
            status.second?.entries?.reversed()?.forEach {
                println(
                    "   ${it.key}: ${it.value.start.toLocalTime()} .. ${it.value.endExclusive.toLocalTime()}"
                )
            }
        }
    }
}