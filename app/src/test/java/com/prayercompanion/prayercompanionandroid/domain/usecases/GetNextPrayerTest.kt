package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.google.common.truth.Truth
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetNextPrayer
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.FakeAppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersFakeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetNextPrayerTest {

    private lateinit var usecase: GetNextPrayer
    private val prayersRepository: PrayersRepository = PrayersFakeRepository()
    private val appLocationManager: AppLocationManager = FakeAppLocationManager()

    @Before
    fun setup() {
        usecase = GetNextPrayer(
            prayersRepository = prayersRepository,
            appLocationManager = appLocationManager
        )
    }

    @Test
    fun `should return the correct next prayer FAJR`() = runTest {
        val currentPrayer = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.ISHA)
        val prayer = usecase.call(currentPrayer)

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.FAJR)
        }
    }

    @Test
    fun `should return the correct next prayer DUHA`() = runTest {
        val currentPrayer = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.FAJR)
        val prayer = usecase.call(currentPrayer)

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.DUHA)
        }

    }

    @Test
    fun `should return the correct next prayer DHUHR`() = runTest {
        val currentPrayer = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.DUHA)
        val prayer = usecase.call(currentPrayer)

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.DHUHR)
        }

    }

    @Test
    fun `should return the correct next prayer ASR`() = runTest {
        val currentPrayer = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.DHUHR)
        val prayer = usecase.call(currentPrayer)

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ASR)
        }
    }

    @Test
    fun `should return the correct next prayer MAGHRIB`() = runTest {
        val currentPrayer = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.ASR)
        val prayer = usecase.call(currentPrayer)

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.MAGHRIB)
        }

    }

    @Test
    fun `should return the correct next prayer ISHA`() = runTest {
        val currentPrayer = PrayersFakeRepository.DEFAULT_DAY_PRAYERS_INFO.get(Prayer.MAGHRIB)
        val prayer = usecase.call(currentPrayer)

        Truth.assertThat(prayer.isSuccess).isTrue()

        prayer.onSuccess {
            Truth.assertThat(it.prayer).isEqualTo(Prayer.ISHA)
        }

    }
}