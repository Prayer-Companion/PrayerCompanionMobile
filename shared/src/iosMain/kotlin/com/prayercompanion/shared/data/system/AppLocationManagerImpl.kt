package com.prayercompanion.shared.data.system

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

class AppLocationManagerImpl : AppLocationManager {

    private val l = MutableStateFlow<Location?>(null)

    init {
        @OptIn(ExperimentalForeignApi::class)
        object : CLLocationManagerDelegateProtocol, NSObject() {
            private val locationManager = CLLocationManager()

            init {
                locationManager.delegate = this
                locationManager.startUpdatingLocation()
            }

            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                notify(didUpdateLocations.lastOrNull() as? CLLocation, manager.heading)
            }

            override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
                notify(manager.location, didUpdateHeading)
            }

            fun notify(lastLocation: CLLocation?, lastHeading: CLHeading?) {
                lastLocation?.coordinate?.useContents {
                    val loc = Location(latitude, longitude)
                    l.value = loc
                }
            }
        }
    }

    override suspend fun getLastKnownLocation(): Location? = withContext(Dispatchers.Main){
        return@withContext l.first()
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