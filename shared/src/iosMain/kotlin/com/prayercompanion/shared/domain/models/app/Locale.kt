package com.prayercompanion.shared.domain.models.app

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual class Locale private constructor(val nsLocale: NSLocale) {
    actual companion object {
        actual fun default(): Locale {
            return Locale(NSLocale.currentLocale)
        }

        actual fun en(): Locale {
            return Locale(NSLocale("en"))
        }
    }
}