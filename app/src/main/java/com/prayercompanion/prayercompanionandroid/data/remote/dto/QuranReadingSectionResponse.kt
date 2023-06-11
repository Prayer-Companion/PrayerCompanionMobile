package com.prayercompanion.prayercompanionandroid.data.remote.dto

import com.squareup.moshi.Json

data class QuranReadingSectionResponse(
    @field:Json(name = "surahId")
    val chapterId: Int,
    @field:Json(name = "startAya")
    val startVerse: Int,
    @field:Json(name = "endAya")
    val endVerse: Int
)
