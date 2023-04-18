package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.squareup.moshi.Json

data class DayPrayerResponse(
    /**
     * Prayers date in dd/MM/yyyy format
     * */
    @field:Json(name = "date")
    val date: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @field:Json(name = "fajr")
    val fajrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @field:Json(name = "sunrise")
    val duhaTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @field:Json(name = "dhuhr")
    val dhuhrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @field:Json(name = "asr")
    val asrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @field:Json(name = "maghrib")
    val maghribTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @field:Json(name = "isha")
    val ishaTime: String,
)