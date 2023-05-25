package com.prayercompanion.prayercompanionandroid.presentation.utils

import java.time.format.DateTimeFormatter
import java.util.Locale

object PresentationConsts {
    val DateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy", Locale.ENGLISH)
    val TimeFormatter: DateTimeFormatter
        get() = DateTimeFormatter
            .ofPattern("hh:mm a", Locale.ENGLISH)
            .withLocale(Locale.getDefault())
}