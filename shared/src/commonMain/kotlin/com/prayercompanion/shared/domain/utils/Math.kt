package com.prayercompanion.shared.domain.utils

object Math {

    private const val DEGREES_TO_RADIANS: Double = 0.017453292519943295
    private const val RADIANS_TO_DEGREES = 57.29577951308232
    fun toRadians(angdeg: Double): Double {
        return angdeg * DEGREES_TO_RADIANS
    }

    fun toDegrees(angrad: Double): Double {
        return angrad * RADIANS_TO_DEGREES
    }

}