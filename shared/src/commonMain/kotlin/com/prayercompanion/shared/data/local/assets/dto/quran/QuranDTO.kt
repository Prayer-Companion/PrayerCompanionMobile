package com.prayercompanion.shared.data.local.assets.dto.quran

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuranDTO(
    @SerialName("sura")
    var chapters: List<QuranChapterDTO>
)