package com.prayercompanion.prayercompanionandroid.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayPrayerResponse(
    /**
     * Prayers date in dd/MM/yyyy format
     * */
    @SerialName("date")
    val date: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerialName("fajr")
    val fajrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerialName("sunrise")
    val sunriseTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerialName("dhuhr")
    val dhuhrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerialName("asr")
    val asrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerialName("maghrib")
    val maghribTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerialName("isha")
    val ishaTime: String,
)