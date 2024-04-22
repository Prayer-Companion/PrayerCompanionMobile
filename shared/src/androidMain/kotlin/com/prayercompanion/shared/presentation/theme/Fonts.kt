package com.prayercompanion.shared.presentation.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.prayercompanion.shared.R

actual val BoldFontFamily: FontFamily = FontFamily(Font(R.font.bold))
actual val RegularFontFamily: FontFamily = FontFamily(Font(R.font.regular, weight = FontWeight.W400))