package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.squareup.moshi.Json

data class MemorizedChapterVersesResponse(
    @field:Json(name = "surahId")
    val chapterId: Int,
    @field:Json(name = "startAya")
    val startVerse: Int,
    @field:Json(name = "endAya")
    val endVerse: Int
)