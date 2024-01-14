package com.prayercompanion.prayercompanionandroid.domain.utils

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address
import com.prayercompanion.shared.domain.repositories.LocationRepository

internal class FakeLocationRepository : LocationRepository {
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