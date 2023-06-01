package com.prayercompanion.prayercompanionandroid.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

fun android.location.Location.toAppLocation(): Location {
    return Location(latitude, longitude)
}