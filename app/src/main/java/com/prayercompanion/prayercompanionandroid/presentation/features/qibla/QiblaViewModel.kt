package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.presentation.utils.OrientationSensor
import com.prayercompanion.prayercompanionandroid.presentation.utils.SensorAccuracy
import com.prayercompanion.shared.domain.utils.AppLocationManager
import com.prayercompanion.shared.domain.utils.QiblaUtils
import kotlinx.coroutines.launch
import kotlin.math.abs

class QiblaViewModel constructor(
    private val appLocationManager: AppLocationManager,
    private val orientationSensor: OrientationSensor,
) : ViewModel() {

    var sensorAccuracy by mutableStateOf(SensorAccuracy.NO_CONTACT)
        private set
    var qiblaDirection by mutableStateOf<Double?>(null)
        private set

    fun onEvent(event: QiblaUiEvent) {
        when (event) {
            QiblaUiEvent.OnStart -> onStart()
            QiblaUiEvent.OnDispose -> onDispose()
        }
    }

    private fun onStart() {
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

    private fun onDispose() {
        orientationSensor.dispose()
    }

    companion object {
        private const val QIBLA_CHANGE_THRESHOLD = 1
    }
}
