package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.squareup.moshi.Json

data class DayPrayerStatusResponse(
    @field:Json(name = "date")
    val date: String = "none",
    @field:Json(name = "fajr")
    val fajr: String = "none",
    @field:Json(name = "sunrise")
    val duha: String = "none",
    @field:Json(name = "dhuhr")
    val dhuhr: String = "none",
    @field:Json(name = "asr")
    val asr: String = "none",
    @field:Json(name = "maghrib")
    val maghrib: String = "none",
    @field:Json(name = "isha")
    val isha: String = "none"
)