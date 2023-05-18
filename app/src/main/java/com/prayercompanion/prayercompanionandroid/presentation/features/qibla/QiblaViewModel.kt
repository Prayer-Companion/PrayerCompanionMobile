package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.OrientationSensor
import com.prayercompanion.prayercompanionandroid.domain.utils.QiblaUtils
import com.prayercompanion.prayercompanionandroid.domain.utils.SensorAccuracy
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Math.abs
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val appLocationManager: AppLocationManager,
    private val orientationSensor: OrientationSensor,
) : ViewModel() {

    var location by mutableStateOf<Location?>(null)
        private set

    var orientation by mutableStateOf(0F)
        private set

    var sensorAccuracy by mutableStateOf(SensorAccuracy.NO_CONTACT)

    var qiblaDirection by mutableStateOf<Double?>(null)

    fun onStart() {
        appLocationManager.getLastKnownLocation { lastLocation ->
            location = lastLocation
        }

        orientationSensor.initialize(
            onOrientationChangedListener = {
                orientation = it

                location?.let { location ->
                    val newQiblaDirection = QiblaUtils.getDirection(
                        location.latitude, location.longitude, orientation.toDouble()
                    )

                    if (abs(newQiblaDirection - (qiblaDirection ?: 0.0)) > 2) {
                        qiblaDirection = newQiblaDirection
                    }


                }
            },
            onAccuracyChangedListener = {
                sensorAccuracy = it
                Log.d("JOSEF" , "sensorAccuracy = ${sensorAccuracy}")
            }
        )
    }

    fun onDispose() {
        orientationSensor.dispose()
    }

}
