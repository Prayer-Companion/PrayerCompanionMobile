package com.prayercompanion.prayercompanionandroid.presentation.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.models.PrayerStatusWithTimeRange
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.shared.domain.models.quran.QuranChapter
import com.prayercompanion.shared.domain.models.quran.QuranVerse
import com.prayercompanion.shared.presentation.features.main.home_screen.components.HomeHeader
import com.prayercompanion.shared.presentation.features.main.home_screen.components.HomeScreenContent
import com.prayercompanion.shared.presentation.features.main.home_screen.components.PrayerItem
import com.prayercompanion.shared.presentation.features.main.home_screen.components.PrayerStatusesOverViewBar
import com.prayercompanion.shared.presentation.features.main.home_screen.components.StatusPickerDialog
import com.prayercompanion.shared.presentation.features.main.quran.components.QuranChapterItem
import com.prayercompanion.shared.presentation.features.main.quran.components.QuranSection
import com.prayercompanion.shared.presentation.features.main.settings.SettingsScreen
import com.prayercompanion.shared.presentation.features.main.settings.SettingsState
import com.prayercompanion.shared.presentation.models.RemainingDuration
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.stringResource
import com.raedghazal.kotlinx_datetime_ext.now
import com.raedghazal.kotlinx_datetime_ext.plus
import kotlinx.coroutines.flow.emptyFlow
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

@Preview(locale = "en", showSystemUi = true)
@Composable
private fun SettingsScreenPreview() = PrayerCompanionAndroidTheme {
    SettingsScreen(SettingsState(), {}, emptyFlow())
}

@Preview(locale = "en")
@Composable
private fun QuranSectionPreview() = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(spacing.spaceMedium)
    ) {
        QuranSection(
            title = stringResource(id = Res.strings.first_quran_reading_section),
            numberOfLines = 1,
            section = PrayerQuranReadingSection(
                sectionId = 0,
                chapterId = 0,
                chapterName = "المدثر",
                startVerse = 0,
                endVerse = 5,
                verses = listOf(
                    QuranVerse(
                        index = 1,
                        text = "يَا أَيُّهَا الْمُدَّثِّرُ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 2,
                        text = "قُمْ فَأَنذِرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 3,
                        text = "وَرَبَّكَ فَكَبِّرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 4,
                        text = "وَثِيَابَكَ فَطَهِّرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 5,
                        text = "وَالرُّجْزَ فَاهْجُرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 6,
                        text = "وَلَا تَمْنُن تَسْتَكْثِرُ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 7,
                        text = "وَلِرَبِّكَ فَاصْبِرْ",
                        hasBismillah = false
                    ),

                    )
            )
        )
    }
}

@Preview(locale = "ar")
@Composable
private fun QuranItemPreview() {
    QuranChapterItem(
        modifier = Modifier.fillMaxWidth(),
        quranChapter = QuranChapter(
            index = 1,
            name = "المدثر",
            verses = listOf()
        ),
        onCheckedChange = { _, _, _ ->

        },
        onSaveClicked = { _, _ ->

        }
    )
}