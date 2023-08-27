package com.prayercompanion.prayercompanionandroid.data.local.assets.dto.quran

import com.google.gson.annotations.SerializedName

data class QuranChapterDTO(
    @SerializedName("_index")
    val index: Int,
    @SerializedName("_name")
    val name: String,
    @SerializedName("aya")
    val verses: List<QuranVerseDTO>
)