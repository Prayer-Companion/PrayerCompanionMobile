package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.OrientationSensor
import com.prayercompanion.prayercompanionandroid.domain.utils.QiblaUtils
import com.prayercompanion.prayercompanionandroid.domain.utils.SensorAccuracy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val appLocationManager: AppLocationManager,
    private val orientationSensor: OrientationSensor,
) : ViewModel() {

    var sensorAccuracy by mutableStateOf(SensorAccuracy.NO_CONTACT)

    var qiblaDirection by mutableStateOf<Double?>(null)

    fun onStart() {
        viewModelScope.launch {
            val location = appLocationManager.getLastKnownLocation()

            orientationSensor.initialize(
                onOrientationChangedListener = { orientation ->
                    location?.let { location ->
                        val newQiblaDirection = QiblaUtils.getDirection(
                            location.latitude, location.longitude, orientation.toDouble()
                        )

                        val qiblaChange = abs(newQiblaDirection - (qiblaDirection ?: 0.0))
                        if (qiblaChange > QIBLA_CHANGE_THRESHOLD) {
                            qiblaDirection = newQiblaDirection
                        }
                    }
                },
                onAccuracyChangedListener = {
                    sensorAccuracy = it
                }
            )
        }

    }

    fun onDispose() {
        orientationSensor.dispose()
    }

    companion object {
        private const val QIBLA_CHANGE_THRESHOLD = 1
    }
}
