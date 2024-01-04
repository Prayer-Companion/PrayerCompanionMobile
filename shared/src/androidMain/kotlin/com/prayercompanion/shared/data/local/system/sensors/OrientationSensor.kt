package com.prayercompanion.shared.data.local.system.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.prayercompanion.shared.domain.models.SensorAccuracy

actual class OrientationSensor constructor(
    context: Context,
) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var lastAccelerometerReading: FloatArray? = null
    private var lastMagnetometerReading: FloatArray? = null

    private var onOrientationChangedListener: (Float) -> Unit = {}
    private var onAccuracyChangedListener: (SensorAccuracy) -> Unit = {}

    actual fun initialize(
        onOrientationChangedListener: (Float) -> Unit,
        onAccuracyChangedListener: (SensorAccuracy) -> Unit,
    ) {
        this.onOrientationChangedListener = onOrientationChangedListener
        this.onAccuracyChangedListener = onAccuracyChangedListener

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            this,
            magnetometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    actual fun dispose() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            lastAccelerometerReading = event.values.clone()
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            lastMagnetometerReading = event.values.clone()
        }

        if (lastAccelerometerReading != null && lastMagnetometerReading != null) {
            val rotationMatrix = FloatArray(9)
            if (SensorManager.getRotationMatrix(
                    rotationMatrix, null, lastAccelerometerReading, lastMagnetometerReading
                )
            ) {
                val orientationMatrix = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientationMatrix)
                onOrientationChangedListener(orientationMatrix[0])
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val sensorAccuracy = sensorAccuracyFromInt(accuracy)
        onAccuracyChangedListener(sensorAccuracy)
    }

    private fun sensorAccuracyFromInt(accuracy: Int): SensorAccuracy {
        return when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> SensorAccuracy.HIGH
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> SensorAccuracy.MEDIUM
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> SensorAccuracy.LOW
            SensorManager.SENSOR_STATUS_UNRELIABLE -> SensorAccuracy.UNRELIABLE
            SensorManager.SENSOR_STATUS_NO_CONTACT -> SensorAccuracy.NO_CONTACT
            else -> SensorAccuracy.NO_CONTACT
        }
    }
}