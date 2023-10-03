package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address

actual class AppLocationManagerImpl {
    actual suspend fun getLastKnownLocation(): Location? {
        TODO()
    }
    actual suspend fun getAddressByLocation(location: Location?): Address? {
        TODO()
    }
    actual fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit) {
        TODO()
    }
}