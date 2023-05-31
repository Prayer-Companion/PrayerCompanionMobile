package com.prayercompanion.prayercompanionandroid.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val countryCode: String,
    val locality: String
)