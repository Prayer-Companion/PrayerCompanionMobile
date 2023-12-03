package com.prayercompanion.prayercompanionandroid.domain.utils

import com.prayercompanion.shared.data.system.AppLocationManager
import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address

internal class FakeAppLocationManager : AppLocationManager {
    override suspend fun getLastKnownLocation(): Location = Location(
        latitude = 31.963158,
        longitude = 35.930359,
    )

    override suspend fun getAddressByLocation(location: Location?): Address = Address(
        countryCode = "Jo", locality = "Amman"
    )

    override fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit) {
        TODO("Not yet implemented")
    }


}