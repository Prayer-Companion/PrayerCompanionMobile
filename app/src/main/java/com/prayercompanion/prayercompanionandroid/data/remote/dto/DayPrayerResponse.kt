package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DayPrayerResponse(
    /**
     * Prayers date in dd/MM/yyyy format
     * */
    @SerializedName("date")
    val date: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerializedName("fajr")
    val fajrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerializedName("sunrise")
    val duhaTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerializedName("dhuhr")
    val dhuhrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerializedName("asr")
    val asrTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerializedName("maghrib")
    val maghribTime: String,
    /**
     * Prayer time in 24h format (HH:mm)
     * */
    @SerializedName("isha")
    val ishaTime: String,
)