package com.prayercompanion.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform