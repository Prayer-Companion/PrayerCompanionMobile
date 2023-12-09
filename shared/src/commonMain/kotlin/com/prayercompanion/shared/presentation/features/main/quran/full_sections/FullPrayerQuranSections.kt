package com.prayercompanion.shared.presentation.features.main.quran.full_sections

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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.presentation.components.AppBackground
import com.prayercompanion.shared.presentation.components.TitleHeader
import com.prayercompanion.shared.presentation.features.main.quran.components.QuranSection
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.stringResource

object FullPrayerQuranSections : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<FullPrayerQuranSectionsViewModel>()
        FullPrayerQuranSections(
            prayerQuranReadingSections = viewModel.quranReadingSections,
            onBack = {
                navigator.pop()
            }
        )
    }
}

@Composable
private fun FullPrayerQuranSections(
    prayerQuranReadingSections: PrayerQuranReadingSections,
    onBack: () -> Unit,
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    AppBackground()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TitleHeader(
            title = stringResource(id = Res.strings.quran_title),
            onBack = onBack
        )
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
                stringResource(id = Res.strings.first_quran_reading_section),
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
                stringResource(id = Res.strings.second_quran_reading_section),
                prayerQuranReadingSections.secondSection
            )
        }

    }
}