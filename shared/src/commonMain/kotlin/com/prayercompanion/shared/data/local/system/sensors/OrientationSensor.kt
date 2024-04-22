package com.prayercompanion.shared.data.local.system.sensors

import com.prayercompanion.shared.domain.models.SensorAccuracy

expect class OrientationSensor {
    fun initialize(
        onOrientationChangedListener: (Float) -> Unit,
        onAccuracyChangedListener: (SensorAccuracy) -> Unit,
    )
    fun dispose()
}