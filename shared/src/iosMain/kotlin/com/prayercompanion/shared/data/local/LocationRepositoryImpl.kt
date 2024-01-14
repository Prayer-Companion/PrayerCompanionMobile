package com.prayercompanion.shared.data.local

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address
import com.prayercompanion.shared.domain.repositories.LocationRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.darwin.NSObject

class LocationRepositoryImpl : LocationRepository {

    private val location = MutableStateFlow<Location?>(null)
    private val address = MutableStateFlow<Address?>(null)

    init {
        LocationManager(
            onLocationRetrieved = { location.value = it },
            onAddressRetrieved = { address.value = it })
    }

    override suspend fun getLastKnownLocation(): Location? =
        withContext(Dispatchers.Main) {
            return@withContext location.first { it != null }
        }

    override suspend fun getAddressByLocation(location: Location?): Address? =
        withContext(Dispatchers.Main) {
            return@withContext address.first { it != null }
        }

    override fun getRequestLocationUpdates(onLocationRetrieved: (Location) -> Unit) = Unit
}

@OptIn(ExperimentalForeignApi::class)
private class LocationManager(
    private val onLocationRetrieved: (location: Location) -> Unit,
    private val onAddressRetrieved: (address: Address) -> Unit
) : CLLocationManagerDelegateProtocol, NSObject() {
    private val locationManager = CLLocationManager()
    private val clGeocoder = CLGeocoder()

    init {
        locationManager.delegate = this
        locationManager.startUpdatingLocation()
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        notify(didUpdateLocations.lastOrNull() as? CLLocation)
    }

    override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
        notify(manager.location)
    }

    fun notify(lastLocation: CLLocation?) {
        lastLocation?.let { location ->
            location.coordinate.useContents {
                onLocationRetrieved(Location(latitude, longitude))
            }

            getAddressForLocation(location) {
                onAddressRetrieved(it)
            }
        }
    }

    private fun getAddressForLocation(location: CLLocation, callback: (Address) -> Unit) {
        return clGeocoder.reverseGeocodeLocation(location) { placeMarks, _ ->
            val place = placeMarks?.firstOrNull() as CLPlacemark?

            val countryCode = place?.ISOcountryCode ?: return@reverseGeocodeLocation
            val locality = place.administrativeArea ?: return@reverseGeocodeLocation

            val address = Address(countryCode, locality)
            callback(address)
        }

    }
}