package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.extensions.plus
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.PrayerStatusWithTimeRange
import com.prayercompanion.shared.presentation.features.home_screen.components.HomeHeader
import com.prayercompanion.shared.presentation.features.home_screen.components.HomeScreenContent
import com.prayercompanion.shared.presentation.features.home_screen.components.PrayerItem
import com.prayercompanion.shared.presentation.features.home_screen.components.PrayerStatusesOverViewBar
import com.prayercompanion.shared.presentation.features.home_screen.components.StatusPickerDialog
import com.prayercompanion.shared.presentation.models.RemainingDuration
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Preview(name = "en", locale = "en")
@Preview(name = "ar", locale = "ar")
@Composable
fun HomeScreenPreview() {
    PrayerCompanionAndroidTheme {
        HomeHeader(
            modifier = Modifier,
            currentPrayer = PrayerInfo.Default,
            nextPrayer = PrayerInfo.Default,
            durationUntilNextPrayer = RemainingDuration(0, 0, 0),
            statusesCounts = listOf(),
            onPrayedNowClicked = { },
            onStatusOverviewBarClicked = { }
        )
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

@Preview(locale = "ar")
@Composable
private fun PrayerItemPreview() {
    PrayerItem(
        modifier = Modifier,
        name = "العصر",
        prayerInfo = PrayerInfo.Default.copy(selectedStatus = PrayerStatus.Jamaah),
        onStatusSelected = { _, _ -> },
        onIshaStatusesPeriodsExplanationClicked = { }
    )
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

@Preview
@Composable
private fun HomeScreenContentPreview() = PrayerCompanionAndroidTheme {
    HomeScreenContent(
        currentPrayerInfo = PrayerInfo.Default,
        nextPrayerInfo = PrayerInfo.Default,
        statusesOverview = listOf(),
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