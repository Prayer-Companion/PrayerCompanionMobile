package com.prayercompanion.shared.domain.repositories

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address

interface LocationRepository {
    suspend fun getLastKnownLocation(): Location?
    suspend fun getAddressByLocation(location: Location?): Address?
    fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit)
}