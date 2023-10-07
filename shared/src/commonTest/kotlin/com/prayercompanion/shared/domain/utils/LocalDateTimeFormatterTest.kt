package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.domain.models.app.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalDateTimeFormatterTest {

    @Test
    fun `test format LocalDateTime FullDateTimeFormatter`() {
        val formatter = FullDateTimeFormatter
        val localDateTime = LocalDateTime(2021, 1, 1, 1, 1)
        assertEquals("01/01/2021 - 01:01", formatter.format(localDateTime))
    }

    @Test
    fun `test format LocalDateTime MonthYearFormatter`() {
        val formatter = MonthYearFormatter
        val localDateTime = LocalDateTime(2021, 1, 1, 1, 1)
        assertEquals("01/2021", formatter.format(localDateTime))
    }

    @Test
    fun `test format LocalDateTime DateFormatter`() {
        val formatter = DateFormatter
        val localDateTime = LocalDate(2021, 1, 1)
        assertEquals("01/01/2021", formatter.format(localDateTime))
    }

    @Test
    fun `test format LocalDateTime TimeFormatter`() {
        val formatter = TimeFormatter
        val localDateTime = LocalTime(1, 1, 1)
        assertEquals("01:01", formatter.format(localDateTime))
    }

    @Test
    fun `test parse to SqlDateTimeFormatter`() {
        val formatter = SqlDateTimeFormatter
        val localDateTime = LocalDateTime(2021, 1, 1, 1, 1)
        assertEquals(formatter.parseToLocalDateTime("2021-01-01 01:01:00").toString(), localDateTime.toString())
    }

    companion object {
        val SqlDateTimeFormatter = LocalDateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.en())
        val FullDateTimeFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
            .ofPattern("dd/MM/yyyy - HH:mm", Locale.en())
        val MonthYearFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
            .ofPattern("MM/yyyy", Locale.en())
        val DateFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
            .ofPattern("dd/MM/yyyy", Locale.en())
        val TimeFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
            .ofPattern("HH:mm", Locale.en())
    }
}