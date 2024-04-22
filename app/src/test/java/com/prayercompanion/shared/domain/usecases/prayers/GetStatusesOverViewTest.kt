package com.prayercompanion.shared.domain.usecases.prayers

import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class GetStatusesOverViewTest {

    private lateinit var usecase: GetStatusesOverView
    private val repository: PrayersRepository = mockk()

    @BeforeTest
    fun setUp() {
        usecase = GetStatusesOverView(repository = repository)
    }

    @Test
    fun call() = runBlocking {
        val expected = listOf(
            PrayerStatus.Jamaah to 8,
            PrayerStatus.OnTime to 5,
            PrayerStatus.AfterHalfTime to 5,
            PrayerStatus.Late to 4,
            PrayerStatus.Missed to 15,
            PrayerStatus.Qadaa to 4,
        )
        val returnedResult = flowOf(
            expected.flatMap {
                val list = mutableListOf<PrayerStatus>()
                for (i in 1..it.second) {
                    list.add(it.first)
                }
                list
            }.shuffled()
        )

        every { repository.getStatusesByDate(any(), any()) } returns returnedResult

        val result = usecase.call()

        assertEquals(expected, result.first())
    }
}