package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.domain.models.app.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

actual class LocalDateTimeFormatter {

    actual fun format(localDateTime: LocalDateTime): String {
        TODO()
    }

    actual fun format(localTime: LocalTime): String {
        TODO()
    }

    actual fun format(localDate: LocalDate): String {
        TODO()
    }

    actual fun parseToLocalDateTime(str: String): LocalDateTime {
        TODO()
    }

    actual fun parseToLocalDate(str: String): LocalDate {
        TODO()
    }

    actual fun parseToLocalTime(str: String): LocalTime {
        TODO()
    }

    actual companion object {
        actual fun ofPattern(format: String, locale: Locale): LocalDateTimeFormatter {
            TODO()
        }
    }
}