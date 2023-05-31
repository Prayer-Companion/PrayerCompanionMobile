package com.prayercompanion.prayercompanionandroid.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)