package com.prayercompanion.prayercompanionandroid.data.local.assets.dto.quran

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuranDTO(
    @SerialName("sura")
    var chapters: List<QuranChapterDTO>
)