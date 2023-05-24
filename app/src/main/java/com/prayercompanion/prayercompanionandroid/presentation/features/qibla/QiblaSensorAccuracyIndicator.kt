package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun QiblaSensorAccuracyIndicator(color: Color = Color.Green, size: Dp = 10.dp) {
    Canvas(modifier = Modifier.size(size)) {
        drawCircle(
            color = color,
            radius = size.toPx() / 2,
            center = center
        )
    }
}