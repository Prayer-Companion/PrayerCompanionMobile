package com.prayercompanion.prayercompanionandroid.data.utils

import java.time.format.DateTimeFormatter
import java.util.*

object DataConsts {
    val MonthYearFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("MM/yyyy", Locale.ENGLISH)
    val DateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy", Locale.ENGLISH)
    val TimeFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("HH:mm", Locale.ENGLISH)
}