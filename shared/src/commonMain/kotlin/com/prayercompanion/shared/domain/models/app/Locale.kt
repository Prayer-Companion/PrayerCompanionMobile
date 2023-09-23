package com.prayercompanion.shared.domain.models.app

expect class Locale {
    companion object {
        fun default(): Locale
        fun en(): Locale
    }
}