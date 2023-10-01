package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address

interface AppLocationManager {
    suspend fun getLastKnownLocation(): Location?
    suspend fun getAddressByLocation(location: Location?): Address?
    fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit)
}

expect class AppLocationManagerImpl {
    suspend fun getLastKnownLocation(): Location?
    suspend fun getAddressByLocation(location: Location?): Address?
    fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit)
}