package com.prayercompanion.prayercompanionandroid.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)

val PrayerStatusJamaahColor = Color(0xFF34C859)
val PrayerStatusOnTimeColor = Color(0xFF33984C)
val PrayerStatusAfterHalfTimeColor = Color(0xFFF3D743)
val PrayerStatusLateColor = Color(0xFFFB9905)
val PrayerStatusMissedColor = Color(0xFFF14F4F)
val PrayerStatusQadaaColor = Color(0xFF9B5832)
val PrayerStatusNotSetColor = Color(0xFF989898)

val SensorAccuracyHigh = Color(0xFF34DF34)
val SensorAccuracyMedium = Color(0xFFFDE14E)
val SensorAccuracyLow = Color(0xFFF14F4F)
val SensorAccuracyNoContact = Color(0xFF000000)

//todo use named colors
private val LightColorPalette = lightColors(
    background = Color(0xFF407E80),
    primary = Color(0xFF2D6061),
    primaryVariant = Color(0xFF429698),
    secondary = Color(0xFFC5C5C5),
    onPrimary = White,
    onSecondary = White,
    surface = Color.Transparent

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