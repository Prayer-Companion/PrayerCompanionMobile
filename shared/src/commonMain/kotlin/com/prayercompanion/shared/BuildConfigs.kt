package com.prayercompanion.shared

expect object BuildConfigs {
    val isDebug: Boolean
    val ISHA_STATUSES_PERIODS_EXPLANATION_URL: String
    val PRAYER_COMPANION_API_BASE_URL: String
}