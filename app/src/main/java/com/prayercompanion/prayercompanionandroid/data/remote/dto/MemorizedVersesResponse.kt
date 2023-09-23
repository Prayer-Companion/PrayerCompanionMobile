package com.prayercompanion.prayercompanionandroid.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemorizedChapterVersesResponse(
    @SerialName("surahId")
    val chapterId: Int,
    @SerialName("startAya")
    val startVerse: Int,
    @SerialName("endAya")
    val endVerse: Int
)