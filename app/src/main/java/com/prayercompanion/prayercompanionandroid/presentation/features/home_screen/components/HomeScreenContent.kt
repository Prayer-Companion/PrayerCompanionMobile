package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.presentation.theme.AppBackground
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts
import java.time.LocalDate
import java.util.SortedMap

@Composable
fun HomeScreenContent(
    currentPrayerInfo: PrayerInfo,
    nextPrayerInfo: PrayerInfo,
    statusesOverview: SortedMap<PrayerStatus, Int>,
    durationUntilNextPrayer: RemainingDuration,
    selectedDate: LocalDate,
    selectedDayPrayersInfo: DayPrayersInfo,
    onPrayedNowClicked: () -> Unit,
    onStatusOverviewBarClicked: () -> Unit,
    onPreviousDayButtonClicked: () -> Unit,
    onNextDayButtonClicked: () -> Unit,
    onStatusSelected: (PrayerStatus, PrayerInfo) -> Unit = { _, _ -> },
    onIshaStatusesPeriodsExplanationClicked: () -> Unit = { }
) {
    val spacing = LocalSpacing.current
    Box {
        AppBackground(
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HomeHeader(
                modifier = Modifier
                    .fillMaxWidth(),
                currentPrayer = currentPrayerInfo,
                nextPrayer = nextPrayerInfo,
                durationUntilNextPrayer = durationUntilNextPrayer,
                statusesCounts = statusesOverview,
                onPrayedNowClicked = onPrayedNowClicked,
                onStatusOverviewBarClicked = onStatusOverviewBarClicked
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            // TODO: add a way to get back to today's date quickly
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .background(
                                color = MaterialTheme.colors.primary,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            onPreviousDayButtonClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "previous day",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                        Text(
                            text = selectedDate.format(PresentationConsts.DateFormatter),
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.onPrimary
                        )
                        IconButton(onClick = {
                            onNextDayButtonClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "next day",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(spacing.spaceMedium, 0.dp)
            ) {
                items(selectedDayPrayersInfo.prayers) {
                    PrayerItem(
                        name = stringResource(id = it.prayer.nameId),
                        modifier = Modifier.fillMaxWidth(),
                        prayerInfo = it,
                        onStatusSelected = onStatusSelected,
                        onIshaStatusesPeriodsExplanationClicked = onIshaStatusesPeriodsExplanationClicked
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenContentPreview() = PrayerCompanionAndroidTheme {
    HomeScreenContent(
        currentPrayerInfo = PrayerInfo.Default,
        nextPrayerInfo = PrayerInfo.Default,
        statusesOverview = sortedMapOf(),
        durationUntilNextPrayer = RemainingDuration(0, 0, 0),
        selectedDate = LocalDate.now(),
        selectedDayPrayersInfo = DayPrayersInfo.Default,
        onPrayedNowClicked = {},
        onPreviousDayButtonClicked = {},
        onNextDayButtonClicked = {},
        onStatusSelected = { _, _ ->  },
        onStatusOverviewBarClicked = {},
        onIshaStatusesPeriodsExplanationClicked = {}
    )
}