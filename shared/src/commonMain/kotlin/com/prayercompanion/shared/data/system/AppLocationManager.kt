package com.prayercompanion.shared.data.system

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address

interface AppLocationManager {
    suspend fun getLastKnownLocation(): Location?
    suspend fun getAddressByLocation(location: Location?): Address?
    suspend fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit)
}

expect class AppLocationManagerImpl: AppLocationManager {
    override suspend fun getLastKnownLocation(): Location?
    override suspend fun getAddressByLocation(location: Location?): Address?
    override suspend fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit)
}