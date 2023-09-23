package com.prayercompanion.prayercompanionandroid.presentation.utils

import com.prayercompanion.shared.domain.models.app.Locale
import com.prayercompanion.shared.domain.utils.LocalDateTimeFormatter

object PresentationConsts {
    val DateFormatter: LocalDateTimeFormatter
        get() = LocalDateTimeFormatter
            .ofPattern("dd/MM/yyyy", Locale.default())
    val TimeFormatter: LocalDateTimeFormatter
        get() = LocalDateTimeFormatter
            .ofPattern("hh:mm a", Locale.default())

    val CounterTimeFormatter: LocalDateTimeFormatter
        get() = LocalDateTimeFormatter
            .ofPattern("HH:mm:ss", Locale.default())
}