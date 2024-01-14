package com.prayercompanion.shared

actual object BuildConfigs {
    actual val isDebug: Boolean = false //TODO("ios")
    actual val ISHA_STATUSES_PERIODS_EXPLANATION_URL: String
        get() = "https://prayer-companion.com/public/isha_prayer_periods_calculations.html"

    actual val PRAYER_COMPANION_API_BASE_URL: String = "https://api.prayer-companion.com/"
}