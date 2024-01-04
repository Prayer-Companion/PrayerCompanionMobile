package com.prayercompanion.shared.data.local.system.sensors

import com.prayercompanion.shared.domain.models.SensorAccuracy

actual class OrientationSensor {
    actual fun initialize(
        onOrientationChangedListener: (Float) -> Unit,
        onAccuracyChangedListener: (SensorAccuracy) -> Unit,
    ) {
        return
        TODO("ios")
    }
    actual fun dispose() {
        return
        TODO("ios")
    }
}