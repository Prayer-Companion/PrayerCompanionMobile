package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.google.common.truth.Truth
import com.prayercompanion.prayercompanionandroid.domain.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetCurrentPrayer
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.FakeAppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersFakeRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class GetCurrentPrayerTest {

    private lateinit var usecase: GetCurrentPrayer
    private val prayersRepository: PrayersRepository = PrayersFakeRepository()
    private val appLocationManager: AppLocationManager = FakeAppLocationManager()
    private val clock: Clock = mockk()

    @Before
    fun setup() {
        usecase = GetCurrentPrayer(
            prayersRepository = prayersRepository,
            appLocationManager = appLocationManager,
            clock = clock
        )
    }

    @Test
    fun `should return the correct current prayer Previous ISHA`() = runTest {
        val time = LocalTime.of(3, 0, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ISHA)
        }
    }

    @Test
    fun `should return the correct current prayer FAJR`() = runTest {
        val time = LocalTime.of(5, 30, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.FAJR)
        }

    }

    @Test
    fun `should return the correct current prayer DUHA`() = runTest {
        val time = LocalTime.of(7, 30, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.DUHA)
        }
    }

    @Test
    fun `should return the correct current prayer DHUHR`() = runTest {
        val time = LocalTime.of(12, 30, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.DHUHR)
        }
    }

    @Test
    fun `should return the correct current prayer ASR`() = runTest {
        val time = LocalTime.of(15, 30, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ASR)
        }

    }

    @Test
    fun `should return the correct current prayer MAGHRIB`() = runTest {
        val time = LocalTime.of(18, 30, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.MAGHRIB)
        }

    }

    @Test
    fun `should return the correct current prayer ISHA`() = runTest {
        val time = LocalTime.of(20, 30, 0)
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ISHA)
        }
    }

    @Test
    fun `should return the correct current prayer FAJR when its on the exact time`() = runTest {
        val time = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.FAJR).time
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.FAJR)
        }

    }

    @Test
    fun `should return the correct current prayer DUHA when its on the exact time`() = runTest {
        val time = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.DUHA).time
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.DUHA)
        }
    }

    @Test
    fun `should return the correct current prayer DHUHR when its on the exact time`() = runTest {
        val time = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.DHUHR).time
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.DHUHR)
        }
    }

    @Test
    fun `should return the correct current prayer ASR when its on the exact time`() = runTest {
        val time = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.ASR).time
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ASR)
        }

    }

    @Test
    fun `should return the correct current prayer MAGHRIB when its on the exact time`() = runTest {
        val time = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.MAGHRIB).time
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.MAGHRIB)
        }

    }

    @Test
    fun `should return the correct current prayer ISHA when its on the exact time`() = runTest {
        val time = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.ISHA).time
        stubClock(LocalDateTime.of(Consts.TODAY_DATE, time))
        val prayer = usecase.call()

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ISHA)
        }
    }

    private fun stubClock(dateTime: LocalDateTime) {
        val newClock = Clock.fixed(
            dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()
        )
        every { clock.instant() } returns newClock.instant()
        every { clock.zone } returns newClock.zone
    }
}