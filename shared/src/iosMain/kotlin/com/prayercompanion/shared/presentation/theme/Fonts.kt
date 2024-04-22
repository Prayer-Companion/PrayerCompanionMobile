package com.prayercompanion.shared.presentation.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface


actual val BoldFontFamily: FontFamily = FontFamily(
    Typeface(loadCustomFont("bold"))
)
actual val RegularFontFamily: FontFamily = FontFamily(
    Typeface(loadCustomFont("regular"))
)

private fun loadCustomFont(name: String): Typeface {
    return Typeface.makeFromName(name, FontStyle.NORMAL)
}