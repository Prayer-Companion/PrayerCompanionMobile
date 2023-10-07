package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Stable
fun Modifier.autoMirror(): Modifier = composed {
    if (LocalLayoutDirection.current == LayoutDirection.Rtl)
        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}

/*
*  @returns a pair of hours and remaining millis
*/
fun Long.millisToHours(): Pair<Long, Long> {
    return this.divideWithRemaining(3600_000)
}

/*
*  @returns a pair of minutes and remaining millis
*/
fun Long.millisToMinutes(): Pair<Long, Long> {
    return this.divideWithRemaining(600_00)
}

/*
*  @returns a pair of seconds and remaining millis
*/
fun Long.millisToSeconds(): Pair<Long, Long> {
    return this.divideWithRemaining(1000)
}

infix fun Long.divideWithRemaining(b: Long): Pair<Long, Long> {
    val division = this / b
    val remainder = this % b
    return Pair(division, remainder)
}