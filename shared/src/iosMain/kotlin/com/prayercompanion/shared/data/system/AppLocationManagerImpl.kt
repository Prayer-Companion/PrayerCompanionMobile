package com.prayercompanion.shared.data.system

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address

class AppLocationManagerImpl: AppLocationManager {
    override suspend fun getLastKnownLocation(): Location? {
        return Location(latitude = 31.963158, longitude = 35.930359)
        TODO("ios")
    }
    override suspend fun getAddressByLocation(location: Location?): Address? {
        return Address(
            countryCode = "JO", locality = "Amman"
        )
        TODO("ios")
    }
    override fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit) {
        return
        TODO("ios")
    }
}