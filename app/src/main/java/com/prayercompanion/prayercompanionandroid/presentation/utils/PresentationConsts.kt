package com.prayercompanion.prayercompanionandroid.presentation.utils

import java.time.format.DateTimeFormatter
import java.util.Locale

object PresentationConsts {
    val DateFormatter: DateTimeFormatter
        get() = DateTimeFormatter
            .ofPattern("dd/MM/yyyy", Locale.getDefault())
    val TimeFormatter: DateTimeFormatter
        get() = DateTimeFormatter
            .ofPattern("hh:mm a", Locale.getDefault())

    val CounterTimeFormatter: DateTimeFormatter
        get() = DateTimeFormatter
            .ofPattern("hh:mm:ss", Locale.getDefault())
}