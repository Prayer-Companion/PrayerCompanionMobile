package com.prayercompanion.shared.domain.models.quran

data class QuranVerse(
    val index: Int,
    val text: String,
    val hasBismillah: Boolean
)
