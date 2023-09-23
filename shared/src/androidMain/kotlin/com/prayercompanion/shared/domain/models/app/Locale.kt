package com.prayercompanion.shared.domain.models.app

actual class Locale private constructor(val javaLocale: java.util.Locale) {
    actual companion object {
        actual fun default(): Locale {
            return Locale(java.util.Locale.getDefault())
        }

        actual fun en(): Locale {
            return Locale(java.util.Locale.ENGLISH)
        }
    }
}