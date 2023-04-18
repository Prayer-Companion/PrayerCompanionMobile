package com.prayercompanion.prayercompanionandroid.data.local

import androidx.room.TypeConverter
import com.prayercompanion.prayercompanionandroid.data.utils.DataConsts
import java.time.LocalDate
import java.time.LocalTime

class PrayerCompanionConverters {

    @TypeConverter
    fun localDateToString(localDate: LocalDate): String {
        return localDate.format(DataConsts.DateFormatter)
    }

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate {
        return LocalDate.parse(value, DataConsts.DateFormatter)
    }

    @TypeConverter
    fun localTimeToString(localDate: LocalTime): String {
        return localDate.format(DataConsts.TimeFormatter)
    }

    @TypeConverter
    fun stringToLocalTime(value: String): LocalTime {
        return LocalTime.parse(value, DataConsts.TimeFormatter)
    }
}
