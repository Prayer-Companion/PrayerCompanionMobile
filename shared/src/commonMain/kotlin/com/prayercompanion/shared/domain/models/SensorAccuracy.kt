package com.prayercompanion.shared.domain.models

import androidx.compose.ui.graphics.Color
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.presentation.theme.SensorAccuracyHigh
import com.prayercompanion.shared.presentation.theme.SensorAccuracyLow
import com.prayercompanion.shared.presentation.theme.SensorAccuracyMedium
import com.prayercompanion.shared.presentation.theme.SensorAccuracyNoContact
import dev.icerock.moko.resources.StringResource

enum class SensorAccuracy(val nameId: StringResource, val color: Color) {
    HIGH(Res.strings.sensor_accuracy_high, SensorAccuracyHigh),
    MEDIUM(Res.strings.sensor_accuracy_medium, SensorAccuracyMedium),
    LOW(Res.strings.sensor_accuracy_low, SensorAccuracyLow),
    UNRELIABLE(Res.strings.sensor_accuracy_low, SensorAccuracyLow),
    NO_CONTACT(
        Res.strings.sensor_accuracy_no_contact,
        SensorAccuracyNoContact
    );
}