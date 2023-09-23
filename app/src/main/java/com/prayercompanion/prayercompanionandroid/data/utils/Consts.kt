package com.prayercompanion.prayercompanionandroid.data.utils

import com.prayercompanion.shared.domain.models.app.Locale
import com.prayercompanion.shared.domain.utils.LocalDateTimeFormatter

object Consts {
    val FullDateTimeFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
        .ofPattern("dd/MM/yyyy - HH:mm", Locale.en())
    val MonthYearFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
        .ofPattern("MM/yyyy", Locale.en())
    val DateFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
        .ofPattern("dd/MM/yyyy", Locale.en())
    val TimeFormatter: LocalDateTimeFormatter = LocalDateTimeFormatter
        .ofPattern("HH:mm", Locale.en())
}