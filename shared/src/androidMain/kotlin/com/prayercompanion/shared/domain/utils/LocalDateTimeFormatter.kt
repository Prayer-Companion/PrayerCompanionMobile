package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.domain.models.app.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter

actual class LocalDateTimeFormatter private constructor(
    private val pattern: String?,
    private val locale: Locale
) {

    actual fun format(localDateTime: LocalDateTime): String {
        return DateTimeFormatter.ofPattern(pattern, locale.javaLocale)
            .format(localDateTime.toJavaLocalDateTime())
    }

    actual fun format(localTime: LocalTime): String {
        return DateTimeFormatter.ofPattern(pattern, locale.javaLocale)
            .format(localTime.toJavaLocalTime())
    }

    actual fun format(localDate: LocalDate): String {
        return DateTimeFormatter.ofPattern(pattern, locale.javaLocale)
            .format(localDate.toJavaLocalDate())
    }

    actual fun parseToLocalDateTime(str: String): LocalDateTime {
        val isoString = DateTimeFormatter.ISO_DATE_TIME.format(
            DateTimeFormatter.ofPattern(
                pattern,
                locale.javaLocale
            ).parse(str)
        )

        return LocalDateTime.parse(isoString)
    }

    actual fun parseToLocalDate(str: String): LocalDate {
        val isoString = DateTimeFormatter.ISO_DATE.format(
            DateTimeFormatter.ofPattern(
                pattern,
                locale.javaLocale
            ).parse(str)
        )

        return LocalDate.parse(isoString)
    }

    actual fun parseToLocalTime(str: String): LocalTime {
        val isoString = DateTimeFormatter.ISO_TIME.format(
            DateTimeFormatter.ofPattern(
                pattern,
                locale.javaLocale
            ).parse(str)
        )

        return LocalTime.parse(isoString)
    }

    actual companion object {
        actual fun ofPattern(format: String, locale: Locale): LocalDateTimeFormatter {
            return LocalDateTimeFormatter(format, locale)
        }
    }
}