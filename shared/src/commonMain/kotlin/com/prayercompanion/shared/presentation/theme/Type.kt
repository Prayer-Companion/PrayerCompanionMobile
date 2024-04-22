package com.prayercompanion.shared.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val SmallTextStyle = TextStyle(
    fontFamily = RegularFontFamily,
    fontSize = 8.sp
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = BoldFontFamily,
        fontSize = 40.sp
    ),
    h2 = TextStyle(
        fontFamily = BoldFontFamily,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontFamily = BoldFontFamily,
        fontSize = 15.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = BoldFontFamily,
        fontSize = 30.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = RegularFontFamily,
        fontSize = 15.sp
    ),
    body1 = TextStyle(
        fontFamily = RegularFontFamily,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
        fontFamily = BoldFontFamily,
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontFamily = BoldFontFamily,
        fontSize = 15.sp
    ),
)