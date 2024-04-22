package com.prayercompanion.shared.domain.models.app

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val countryCode: String,
    val locality: String
)