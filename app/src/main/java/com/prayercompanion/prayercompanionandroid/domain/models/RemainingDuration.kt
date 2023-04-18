package com.prayercompanion.prayercompanionandroid.domain.models

import java.util.concurrent.TimeUnit

data class RemainingDuration(
    var hours: Long,
    var minutes: Long,
    var seconds: Long
) {
    companion object {
        fun fromMilliSeconds(millis: Long): RemainingDuration {
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(hours)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes)  - TimeUnit.HOURS.toSeconds(hours)
            return RemainingDuration(hours, minutes, seconds)
        }
    }
}
