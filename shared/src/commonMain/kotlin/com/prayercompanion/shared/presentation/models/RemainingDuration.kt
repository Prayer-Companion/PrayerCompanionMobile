package com.prayercompanion.shared.presentation.models

import com.prayercompanion.shared.presentation.utils.PresentationConsts
import com.prayercompanion.shared.presentation.utils.millisToHours
import com.prayercompanion.shared.presentation.utils.millisToMinutes
import com.prayercompanion.shared.presentation.utils.millisToSeconds
import kotlinx.datetime.LocalTime

data class RemainingDuration(
    var hours: Long,
    var minutes: Long,
    var seconds: Long
) {

    override fun toString(): String {
        val time = LocalTime(hours.toInt(), minutes.toInt(), seconds.toInt())
        return PresentationConsts.CounterTimeFormatter.format(time)
    }

    companion object {
        fun fromMilliSeconds(millis: Long): RemainingDuration {
            var remaining = millis
            val hours = remaining
                .millisToHours()
                .let { (hours, remainingMillis) ->
                    remaining = remainingMillis
                    hours
                }

            val minutes = remaining
                .millisToMinutes()
                .let { (minutes, remainingMillis) ->
                    remaining = remainingMillis
                    minutes
                }

            val seconds = remaining
                .millisToSeconds()
                .let { (seconds, _) ->
                    seconds
                }

            return RemainingDuration(hours, minutes, seconds)
        }
    }
}
