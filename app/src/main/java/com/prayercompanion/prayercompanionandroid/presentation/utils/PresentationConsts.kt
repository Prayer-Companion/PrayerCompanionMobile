package com.prayercompanion.prayercompanionandroid.presentation.utils

import java.time.format.DateTimeFormatter
import java.util.*

object PresentationConsts {
    val DateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy", Locale.ENGLISH)
    val TimeFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("hh:mm a", Locale.ENGLISH)
}