package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts
import com.prayercompanion.prayercompanionandroid.presentation.utils.getPrayerStatusCorrespondingColor
import com.prayercompanion.prayercompanionandroid.presentation.utils.getPrayerStatusNameStringRes
import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.extensions.plus
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.PrayerStatusWithTimeRange
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime


@Composable
fun StatusPickerDialog(
    statusesWithTimeRanges: List<PrayerStatusWithTimeRange>,
    showExplanation: Boolean,
    onItemSelected: (PrayerStatus) -> Unit,
    onIshaStatusesPeriodsExplanationClicked: () -> Unit,
    onDismissRequest: () -> Unit
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.onPrimary,
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PrayerStatusDialogHeader(modifier = Modifier.fillMaxWidth())
                Divider(
                    modifier = Modifier.height(0.5.dp),
                    color = Color.Black.copy(alpha = 0.5f)
                )
                statusesWithTimeRanges.forEach { (status, timeRange, prayer) ->
                    PrayerStatusDialogItem(
                        status = status,
                        text = stringResource(id = getPrayerStatusNameStringRes(status, prayer)),
                        timeRange = timeRange,
                        onItemSelected = onItemSelected
                    )
                    Divider(
                        modifier = Modifier.height(0.5.dp),
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
                if (showExplanation) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colors.secondary)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) Run@{
                        Text(
                            modifier = Modifier.clickable {
                                onIshaStatusesPeriodsExplanationClicked()
                            },
                            text = stringResource(id = R.string.prayerStatusDialog_explanation),
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.onSecondary,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Outlined.Info,
                            tint = MaterialTheme.colors.onSecondary,
                            contentDescription = "Info",
                        )

                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerStatusDialogHeader(modifier: Modifier = Modifier) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colors.primary)
            .padding(vertical = spacing.spaceMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.prayerStatusDialog_title),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun PrayerStatusDialogItem(
    status: PrayerStatus,
    text: String,
    onItemSelected: (PrayerStatus) -> Unit,
    timeRange: OpenEndRange<LocalDateTime>?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onItemSelected(status) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = getPrayerStatusCorrespondingColor(status),
            style = MaterialTheme.typography.h2
        )
        if (timeRange != null) {
            val start = PresentationConsts.TimeFormatter.format(timeRange.start)
            val end =
                PresentationConsts.TimeFormatter.format(timeRange.endExclusive)
            Text(
                text = "$start - $end",
                color = getPrayerStatusCorrespondingColor(status),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Preview
@Composable
private fun PrayerStatusDialogPreview() = PrayerCompanionAndroidTheme {
    StatusPickerDialog(
        statusesWithTimeRanges = listOf(
            PrayerStatusWithTimeRange(
                prayerStatus = PrayerStatus.Jamaah,
                range = LocalDateTime.now()..<LocalDateTime.now().plus(1, DateTimeUnit.HOUR),
                prayer = Prayer.DHUHR
            )
        ),
        onItemSelected = {},
        onDismissRequest = {},
        onIshaStatusesPeriodsExplanationClicked = {},
        showExplanation = true
    )
}