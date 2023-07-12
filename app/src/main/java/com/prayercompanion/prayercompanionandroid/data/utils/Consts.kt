package com.prayercompanion.prayercompanionandroid.data.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Consts {
    val FullDateTimeFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy - HH:mm", Locale.ENGLISH)
    val MonthYearFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("MM/yyyy", Locale.ENGLISH)
    val DateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy", Locale.ENGLISH)
    val TimeFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("HH:mm", Locale.ENGLISH)

    const val TOKEN_UPDATE_THRESHOLD_TIME_MS = 30 * 60 * 1000
    var userTokenUpdateTime: LocalDateTime? = null
    var userToken: String? = null
}