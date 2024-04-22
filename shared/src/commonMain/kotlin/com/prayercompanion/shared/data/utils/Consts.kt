package com.prayercompanion.shared.data.utils

import com.raedghazal.kotlinx_datetime_ext.LocalDateTimeFormatter
import com.raedghazal.kotlinx_datetime_ext.Locale

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