package com.prayercompanion.prayercompanionandroid.domain.models

import androidx.annotation.StringRes
import com.prayercompanion.prayercompanionandroid.R

enum class Prayer(@StringRes val nameId: Int) {
    FAJR(R.string.fajr),
    DHUHR(R.string.dhuhr),
    ASR(R.string.asr),
    MAGHRIB(R.string.maghrib),
    ISHA(R.string.isha);
}
