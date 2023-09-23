package com.prayercompanion.prayercompanionandroid.presentation.models

import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts
import kotlinx.datetime.LocalTime
import kotlin.time.DurationUnit
import kotlin.time.toTimeUnit

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
            val hours = DurationUnit.MILLISECONDS.toTimeUnit().toHours(millis)

            val minutes = DurationUnit.MILLISECONDS.toTimeUnit().toMinutes(millis) -
                DurationUnit.HOURS.toTimeUnit().toMinutes(hours)

            val seconds =
                DurationUnit.MILLISECONDS.toTimeUnit().toSeconds(millis) -
                    DurationUnit.MINUTES.toTimeUnit().toSeconds(minutes) -
                    DurationUnit.HOURS.toTimeUnit().toSeconds(hours)
            return RemainingDuration(hours, minutes, seconds)
        }
    }
}
