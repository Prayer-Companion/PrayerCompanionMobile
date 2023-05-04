package com.prayercompanion.prayercompanionandroid.domain.models

import androidx.annotation.StringRes
import com.prayercompanion.prayercompanionandroid.R

enum class Prayer(@StringRes val nameId: Int) {
    FAJR(R.string.fajr),
    DUHA(R.string.duha),
    DHUHR(R.string.dhuhr),
    ASR(R.string.asr),
    MAGHRIB(R.string.maghrib),
    ISHA(R.string.isha);

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
