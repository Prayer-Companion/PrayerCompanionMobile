package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.presentation.utils.getPrayerStatusCorrespondingColor
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme

@Composable
fun PrayerStatusesOverViewBar(
    modifier: Modifier = Modifier,
    statusesCounts: List<Pair<PrayerStatus, Int>> = listOf()
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

@Preview(name = "Left-To-Right", locale = "en")
@Preview(name = "Right-To-Left", locale = "ar")
@Composable
private fun PrayerStatusesOverViewBarPreview() {
    PrayerStatusesOverViewBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        statusesCounts = listOf(
            PrayerStatus.Jamaah to 8,
            PrayerStatus.OnTime to 5,
            PrayerStatus.AfterHalfTime to 5,
            PrayerStatus.Late to 4,
            PrayerStatus.Qadaa to 4,
            PrayerStatus.Missed to 15,
        )
    )
}