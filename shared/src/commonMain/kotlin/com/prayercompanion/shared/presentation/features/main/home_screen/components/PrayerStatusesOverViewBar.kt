package com.prayercompanion.shared.presentation.features.main.home_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.LayoutDirection
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.getPrayerStatusCorrespondingColor

@Composable
fun PrayerStatusesOverViewBar(
    modifier: Modifier,
    statusesCounts: List<Pair<PrayerStatus, Int>>
) = PrayerCompanionAndroidTheme {
    val emptyColor = MaterialTheme.colors.secondary
    Canvas(modifier = modifier) {
        val isLTR = layoutDirection == LayoutDirection.Ltr
        val totalStatuses = statusesCounts.sumOf { it.second }.toFloat()
        var accumulatedValue = totalStatuses

        if (statusesCounts.isEmpty()) {
            drawRoundRect(
                color = emptyColor,
                size = Size(
                    width = size.width,
                    height = size.height,
                ),
                cornerRadius = CornerRadius(100f)
            )
        } else {
            statusesCounts.forEach { (key, value) ->
                val componentWidth = (accumulatedValue / totalStatuses) * size.width
                val startOffset = if (isLTR) {
                    0f
                } else {
                    size.width - componentWidth
                }

                drawRoundRect(
                    color = getPrayerStatusCorrespondingColor(key),
                    size = Size(
                        width = componentWidth,
                        height = size.height,
                    ),
                    cornerRadius = CornerRadius(100f),
                    topLeft = Offset(startOffset, 0f)
                )
                accumulatedValue -= value
            }
        }
    }

}