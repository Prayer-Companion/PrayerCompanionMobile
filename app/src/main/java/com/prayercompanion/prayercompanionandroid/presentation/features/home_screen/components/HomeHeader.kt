package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts

@Preview(locale = "ar")
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    prayerInfo: PrayerInfo = PrayerInfo.Default,
    durationUntilNextPrayer: RemainingDuration = RemainingDuration(0, 0, 0),
    onStatusSelected: (PrayerStatus, PrayerInfo) -> Unit = { _,_->}
) {
    val spacing = LocalSpacing.current
    Surface(
        modifier = modifier,
        elevation = 15.dp,
        shape = RoundedCornerShape(
            bottomStart = 50.dp,
            bottomEnd = 50.dp
        ),
        color = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.primary.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(spacing.spaceLarge),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(id = prayerInfo.prayer.nameId),
                            style = MaterialTheme.typography.h1,
                            color = MaterialTheme.colors.onPrimary
                        )
                        Text(
                            text = prayerInfo.time.format(PresentationConsts.TimeFormatter),
                            style = MaterialTheme.typography.h1,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                    PrayerStatusPicker {
                        onStatusSelected(it, prayerInfo)
                    }
                }
                Text(
                    text = stringResource(
                        id = R.string.remaining_time,
                        durationUntilNextPrayer.hours,
                        durationUntilNextPrayer.minutes,
                        durationUntilNextPrayer.seconds,
                    ),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}