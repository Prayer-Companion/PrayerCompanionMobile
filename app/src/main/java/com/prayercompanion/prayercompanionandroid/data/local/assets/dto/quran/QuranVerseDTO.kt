package com.prayercompanion.prayercompanionandroid.data.local.assets.dto.quran

import com.google.gson.annotations.SerializedName

data class QuranVerseDTO(
    @SerializedName("_bismillah")
    val bismillah: String? = "",
    @SerializedName("_index")
    val index: Int,
    @SerializedName("_text")
    val text: String
)