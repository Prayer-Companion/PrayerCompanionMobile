package com.prayercompanion.prayercompanionandroid.domain.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_HIGH
import android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_LOW
import android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM
import android.hardware.SensorManager.SENSOR_STATUS_NO_CONTACT
import android.hardware.SensorManager.SENSOR_STATUS_UNRELIABLE
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.SensorAccuracyHigh
import com.prayercompanion.prayercompanionandroid.presentation.theme.SensorAccuracyLow
import com.prayercompanion.prayercompanionandroid.presentation.theme.SensorAccuracyMedium
import com.prayercompanion.prayercompanionandroid.presentation.theme.SensorAccuracyNoContact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OrientationSensor @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var lastAccelerometerReading: FloatArray? = null
    private var lastMagnetometerReading: FloatArray? = null

    private var onOrientationChangedListener: (Float) -> Unit = {}
    private var onAccuracyChangedListener: (SensorAccuracy) -> Unit = {}

    fun initialize(
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

    fun dispose() {
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
        val sensorAccuracy = SensorAccuracy.fromInt(accuracy)
        onAccuracyChangedListener(sensorAccuracy)
    }

}

enum class SensorAccuracy(val accuracy: Int, @StringRes val nameId: Int, val color: Color) {
    HIGH(SENSOR_STATUS_ACCURACY_HIGH, R.string.sensor_accuracy_high, SensorAccuracyHigh),
    MEDIUM(SENSOR_STATUS_ACCURACY_MEDIUM, R.string.sensor_accuracy_medium, SensorAccuracyMedium),
    LOW(SENSOR_STATUS_ACCURACY_LOW, R.string.sensor_accuracy_low, SensorAccuracyLow),
    UNRELIABLE(SENSOR_STATUS_UNRELIABLE, R.string.sensor_accuracy_low, SensorAccuracyLow),
    NO_CONTACT(
        SENSOR_STATUS_NO_CONTACT,
        R.string.sensor_accuracy_no_contact,
        SensorAccuracyNoContact
    );

    companion object {
        fun fromInt(accuracy: Int): SensorAccuracy {
            return values().firstOrNull { it.accuracy == accuracy } ?: NO_CONTACT
        }
    }

}
