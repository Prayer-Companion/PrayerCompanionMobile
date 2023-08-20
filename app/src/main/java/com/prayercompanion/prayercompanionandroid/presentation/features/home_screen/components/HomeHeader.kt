package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.compose.MeasureUnconstrainedViewWidth
import java.util.SortedMap

@Preview(name = "en", locale = "en")
@Preview(name = "ar", locale = "ar")
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    currentPrayer: PrayerInfo = PrayerInfo.Default,
    nextPrayer: PrayerInfo = PrayerInfo.Default,
    durationUntilNextPrayer: RemainingDuration = RemainingDuration(0, 0, 0),
    onPrayedNowClicked: () -> Unit = { },
    statusesCounts: SortedMap<PrayerStatus, Int> = sortedMapOf()
) = PrayerCompanionAndroidTheme {

    val spacing = LocalSpacing.current
    Surface(
        modifier = modifier,
        elevation = 15.dp,
        shape = RoundedCornerShape(
            bottomStart = 50.dp,
            bottomEnd = 50.dp
        ),
        color = Color.Black,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF397778),
                            Color(0xFF2d6061)
                        )
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(top = spacing.spaceMedium)
                    .padding(spacing.spaceLarge),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = stringResource(id = R.string.current_prayer),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.secondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = currentPrayer.prayer.nameId),
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onPrimary
                    )
                    if (currentPrayer.isStateSelectable) {
                        Button(
                            onClick = { onPrayedNowClicked() },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primaryVariant
                            ),
                            contentPadding = PaddingValues(
                                vertical = 3.dp,
                                horizontal = 24.dp,
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.notification_action_prayedNow),
                                style = MaterialTheme.typography.button,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }

                Row {
                    MeasureUnconstrainedViewWidth(
                        modifier = Modifier
                            .alignByBaseline(),
                        viewToMeasure = {
                            Text(
                                modifier = Modifier.alignByBaseline(),
                                text = durationUntilNextPrayer.copy(seconds = 0).toString(),
                                style = MaterialTheme.typography.subtitle1,
                            )
                        }) {
                        Text(
                            modifier = Modifier
                                .defaultMinSize(minWidth = it),
                            text = durationUntilNextPrayer.toString(),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Left
                        )
                    }
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = stringResource(
                            id = R.string.remaining_time,
                            stringResource(id = nextPrayer.prayer.nameId)
                        ),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.secondary,
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Text(
                    text = stringResource(id = R.string.status_overview),
                    style = MaterialTheme.typography.subtitle2,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                PrayerStatusesOverViewBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    statusesCounts = statusesCounts
                )
            }
        }
    }
}