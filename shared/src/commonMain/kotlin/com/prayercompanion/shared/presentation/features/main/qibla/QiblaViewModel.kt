package com.prayercompanion.shared.presentation.features.main.qibla

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.prayercompanion.shared.data.local.system.sensors.OrientationSensor
import com.prayercompanion.shared.data.system.AppLocationManager
import com.prayercompanion.shared.domain.models.SensorAccuracy
import com.prayercompanion.shared.domain.utils.QiblaUtils
import kotlinx.coroutines.launch
import kotlin.math.abs

class QiblaViewModel constructor(
    private val appLocationManager: AppLocationManager,
    private val orientationSensor: OrientationSensor,
) : ScreenModel {

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
        screenModelScope.launch {
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

    override fun onDispose() {
        orientationSensor.dispose()
    }

    companion object {
        private const val QIBLA_CHANGE_THRESHOLD = 1
    }
}
