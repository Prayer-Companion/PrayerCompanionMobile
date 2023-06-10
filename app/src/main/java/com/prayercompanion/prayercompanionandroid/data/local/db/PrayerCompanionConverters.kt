package com.prayercompanion.prayercompanionandroid.data.local.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PrayerCompanionConverters {

    @TypeConverter
    fun localDateToString(localDateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern(SQL_DATE_TIME_FORMAT, Locale.ENGLISH)
        return localDateTime.format(formatter)
    }

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(SQL_DATE_TIME_FORMAT, Locale.ENGLISH)
        return LocalDateTime.parse(value, formatter)
    }

    companion object {
        private const val SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}
