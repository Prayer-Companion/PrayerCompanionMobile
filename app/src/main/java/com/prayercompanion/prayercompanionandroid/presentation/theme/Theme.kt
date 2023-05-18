package com.prayercompanion.prayercompanionandroid.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val PaleGreenishBlue = Color(0xFFC6DFD4)
val White = Color(0xFFFFFFFF)
val DarkGrey = Color(0xFF747474)

val PrayerStatusJamaah = Color(0xFF34DF34)
val PrayerStatusOnTime = Color(0xFFFDE14E)
val PrayerStatusLate = Color(0xFFEBA26D)
val PrayerStatusMissed = Color(0xFFF14F4F)
val PrayerStatusQadaa = Color(0xFF9B5832)
val PrayerStatusNotSet = Color(0xFF989898)

val SensorAccuracyHigh = Color(0xFF34DF34)
val SensorAccuracyMedium = Color(0xFFFDE14E)
val SensorAccuracyLow = Color(0xFFF14F4F)
val SensorAccuracyNoContact = Color(0xFF000000)

//todo use named colors
private val LightColorPalette = lightColors(
    background = Color(0xFF407E80),
    primary = Color(0xFF2D6061),
    primaryVariant = PaleGreenishBlue,
    secondary = Color(0xFFC5C5C5),
    onPrimary = White,
    onSecondary = White,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PrayerCompanionAndroidTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colors = LightColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }

}