package com.prayercompanion.shared.domain.extensions

import com.prayercompanion.shared.domain.models.Location

fun android.location.Location.toAppLocation(): Location {
    return Location(latitude, longitude)
}