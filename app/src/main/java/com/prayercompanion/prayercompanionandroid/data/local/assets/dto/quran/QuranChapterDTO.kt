package com.prayercompanion.prayercompanionandroid.data.local.assets.dto.quran

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuranChapterDTO(
    @SerialName("_index")
    val index: Int,
    @SerialName("_name")
    val name: String,
    @SerialName("aya")
    val verses: List<QuranVerseDTO>
)