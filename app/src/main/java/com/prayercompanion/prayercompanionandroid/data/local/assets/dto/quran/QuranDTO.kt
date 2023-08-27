package com.prayercompanion.prayercompanionandroid.data.local.assets.dto.quran

import com.google.gson.annotations.SerializedName

data class QuranDTO(
    @SerializedName("sura")
    var chapters: List<QuranChapterDTO>
)