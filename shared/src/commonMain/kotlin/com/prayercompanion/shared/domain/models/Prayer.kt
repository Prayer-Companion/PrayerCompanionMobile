package com.prayercompanion.shared.domain.models

enum class Prayer {
    FAJR,
    DUHA, //DB entity name is currently duha have to do a migration schema to sunrise
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA;

    fun next(): Prayer? {
        return when (this) {
            FAJR -> DUHA
            DUHA -> DHUHR
            DHUHR -> ASR
            ASR -> MAGHRIB
            MAGHRIB -> ISHA
            ISHA -> null
        }
    }
}
