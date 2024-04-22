package com.prayercompanion.shared.domain.utils

data class Task<T> constructor(
    val isSuccessful: Boolean,
    val result: T?,
    val exception: Exception?
)