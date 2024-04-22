package com.prayercompanion.shared.data.local.assets.dto.quran

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuranVerseDTO(
    @SerialName("_bismillah")
    val bismillah: String? = "",
    @SerialName("_index")
    val index: Int,
    @SerialName("_text")
    val text: String
)