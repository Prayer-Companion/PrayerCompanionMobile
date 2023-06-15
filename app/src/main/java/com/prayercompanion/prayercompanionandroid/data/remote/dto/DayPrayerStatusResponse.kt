package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DayPrayerStatusResponse(
    @SerializedName("date")
    val date: String = "none",
    @SerializedName("fajr")
    val fajr: String = "none",
    @SerializedName("sunrise")
    val duha: String = "none",
    @SerializedName("dhuhr")
    val dhuhr: String = "none",
    @SerializedName("asr")
    val asr: String = "none",
    @SerializedName("maghrib")
    val maghrib: String = "none",
    @SerializedName("isha")
    val isha: String = "none"
)