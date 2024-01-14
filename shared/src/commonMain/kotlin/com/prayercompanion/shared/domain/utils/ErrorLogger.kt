package com.prayercompanion.shared.domain.utils

interface ErrorLogger {
    fun logException(e: Exception)
}