package com.prayercompanion.prayercompanionandroid.presentation.features.quran.full_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.presentation.components.TitleHeader
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.components.QuranSection
import com.prayercompanion.prayercompanionandroid.presentation.theme.AppBackground
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Preview(locale = "ar")
@Composable
fun FullPrayerQuranSections(
    prayerQuranReadingSections: PrayerQuranReadingSections = PrayerQuranReadingSections.EMPTY
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    AppBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TitleHeader(title = stringResource(id = R.string.quran_title))
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
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
                modifier = Modifier,
                stringResource(id = R.string.first_quran_reading_section),
                prayerQuranReadingSections.firstSection
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing.spaceMedium, bottom = spacing.spaceSmall)
                .padding(horizontal = spacing.spaceMedium)
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(spacing.spaceMedium)
        ) {
            QuranSection(
                modifier = Modifier,
                stringResource(id = R.string.second_quran_reading_section),
                prayerQuranReadingSections.secondSection
            )
        }

    }
}