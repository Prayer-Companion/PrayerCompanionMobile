package com.prayercompanion.shared.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val countryCode: String,
    val locality: String
)