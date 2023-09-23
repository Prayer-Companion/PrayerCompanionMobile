package com.prayercompanion.prayercompanionandroid.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayPrayerStatusResponse(
    @SerialName("date")
    val date: String = "none",
    @SerialName("fajr")
    val fajr: String = "none",
    @SerialName("sunrise")
    val sunrise: String = "none",
    @SerialName("dhuhr")
    val dhuhr: String = "none",
    @SerialName("asr")
    val asr: String = "none",
    @SerialName("maghrib")
    val maghrib: String = "none",
    @SerialName("isha")
    val isha: String = "none"
)