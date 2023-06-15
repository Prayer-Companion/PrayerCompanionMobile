package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MemorizedChapterVersesResponse(
    @SerializedName("surahId")
    val chapterId: Int,
    @SerializedName("startAya")
    val startVerse: Int,
    @SerializedName("endAya")
    val endVerse: Int
)