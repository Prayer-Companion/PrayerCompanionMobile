package com.prayercompanion.prayercompanionandroid.domain.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object QiblaUtils {
    private const val QIBLA_LATITUDE = 21.4224779
    private const val QIBLA_LONGITUDE = 39.8251832

    /**
     * Calculates the Qibla direction from the given latitude and longitude coordinates.
     *
     * @param latitude The latitude of the current location.
     * @param longitude The longitude of the current location.
     * @param phoneOrientation The phone's orientation in radians.
     * @return The Qibla direction in degrees.
     */
    fun getDirection(latitude: Double, longitude: Double, phoneOrientation: Double = 0.0): Double {
        val phiK = Math.toRadians(QIBLA_LATITUDE)
        val lambdaK = Math.toRadians(QIBLA_LONGITUDE)
        val phi = Math.toRadians(latitude)
        val lambda = Math.toRadians(longitude)

        val psi = atan2(
            sin(lambdaK - lambda),
            (cos(phi) * tan(phiK)) - (sin(phi) * cos(lambdaK - lambda))
        )

        var qiblaDirection = Math.toDegrees(psi)
        if (qiblaDirection < 0) {
            qiblaDirection += 360
        }

        // Adjust for phone's orientation
        qiblaDirection -= Math.toDegrees(phoneOrientation)

        return qiblaDirection
    }
}