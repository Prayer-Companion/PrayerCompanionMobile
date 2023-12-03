package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.domain.extensions.min
import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.models.app.Locale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atTime
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter

@OptIn(ExperimentalForeignApi::class)
actual class LocalDateTimeFormatter private constructor(
    private val pattern: String,
    private val locale: Locale
) {

    private val formatter: NSDateFormatter by lazy {
        NSDateFormatter().apply {
            dateFormat = pattern
            locale = this@LocalDateTimeFormatter.locale.nsLocale
        }
    }

    actual fun format(localDateTime: LocalDateTime): String {
        val date = localDateTime.toNsDate() ?: throw IllegalStateException("Failed to convert LocalDateTime $LocalDateTime to NSDate")
        return NSDateFormatter().apply {
            dateFormat = pattern
            locale = this@LocalDateTimeFormatter.locale.nsLocale
        }.stringFromDate(date)
    }

    private fun LocalDateTime.toNsDate(): NSDate? {
        val calendar = NSCalendar.currentCalendar
        val components = NSDateComponents()
        components.year = this.year.convert()
        components.month = this.monthNumber.convert()
        components.day = this.dayOfMonth.convert()
        components.hour = this.hour.convert()
        components.minute = this.minute.convert()
        components.second = this.second.convert()
        return calendar.dateFromComponents(components)
    }

    actual fun format(localTime: LocalTime): String {
        val dateTime = localTime.atDate(LocalDate.now())
        return format(dateTime)
    }

    actual fun format(localDate: LocalDate): String {
        val dateTime = localDate.atTime(LocalTime.min())
        return format(dateTime)
    }

    actual fun parseToLocalDateTime(str: String): LocalDateTime {
        return formatter
            .dateFromString(str)
            ?.toKotlinInstant()
            ?.toLocalDateTime(TimeZone.currentSystemDefault())
            ?: throw IllegalStateException("Failed to convert String $str to LocalDateTime")
    }

    actual fun parseToLocalDate(str: String): LocalDate {
        return formatter
            .dateFromString(str)
            ?.toKotlinInstant()
            ?.toLocalDateTime(TimeZone.currentSystemDefault())
            ?.date
            ?: throw IllegalStateException("Failed to convert String $str to LocalDate")
    }

    actual fun parseToLocalTime(str: String): LocalTime {
        return formatter
            .dateFromString(str)
            ?.toKotlinInstant()
            ?.toLocalDateTime(TimeZone.currentSystemDefault())
            ?.time
            ?: throw IllegalStateException("Failed to convert String $str to LocalTime")
    }

    actual companion object {
        actual fun ofPattern(format: String, locale: Locale): LocalDateTimeFormatter {
            return LocalDateTimeFormatter(format, locale)
        }
    }
}