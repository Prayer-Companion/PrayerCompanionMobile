package com.prayercompanion.prayercompanionandroid.domain.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OrientationSensor @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var lastAccelerometerReading: FloatArray? = null
    private var lastMagnetometerReading: FloatArray? = null

    private var onOrientationChangedListener: (Float) -> Unit = {}

    init {
        Log.d("JOSEF" , "INIT")
    }

    fun initialize(onOrientationChangedListener: (Float) -> Unit){
        this.onOrientationChangedListener = onOrientationChangedListener

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

    fun dispose(){
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
        // Do nothing
    }
}