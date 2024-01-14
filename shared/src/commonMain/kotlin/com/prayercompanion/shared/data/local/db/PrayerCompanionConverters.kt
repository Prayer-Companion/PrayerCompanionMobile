package com.prayercompanion.shared.data.local.db

import com.raedghazal.kotlinx_datetime_ext.LocalDateTimeFormatter
import com.raedghazal.kotlinx_datetime_ext.Locale
import kotlinx.datetime.LocalDateTime

class PrayerCompanionConverters {

    fun localDateToString(localDateTime: LocalDateTime): String {
        val formatter = LocalDateTimeFormatter.ofPattern(SQL_DATE_TIME_FORMAT, Locale.en())
        return formatter.format(localDateTime)
    }

    fun stringToLocalDate(value: String): LocalDateTime {
        val formatter = LocalDateTimeFormatter.ofPattern(SQL_DATE_TIME_FORMAT, Locale.en())
        return formatter.parseToLocalDateTime(value)
    }

    companion object {
        private const val SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}