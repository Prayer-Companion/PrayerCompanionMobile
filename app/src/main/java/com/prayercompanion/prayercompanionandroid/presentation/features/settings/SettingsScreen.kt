package com.prayercompanion.prayercompanionandroid.presentation.features.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import com.prayercompanion.prayercompanionandroid.presentation.components.TitleHeader
import com.prayercompanion.prayercompanionandroid.presentation.theme.AppBackground
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit
) {
    val spacing = LocalSpacing.current

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground()
        Column(modifier = Modifier.fillMaxSize()) {
            TitleHeader(
                title = stringResource(id = R.string.settings_title)
            )
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            Column(modifier = Modifier.padding(horizontal = spacing.spaceLarge)) {
                Text(
                    text = stringResource(id = R.string.settings_items_language),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    LanguageSelector(
                        modifier = Modifier.fillMaxWidth(),
                        selectedLanguage = state.language,
                        onLanguageSelected = {
                            onEvent(SettingsEvent.OnLanguageSelected(it))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageSelector(
    modifier: Modifier = Modifier,
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    Row(modifier = modifier) {
        val arabicBackgroundColor = if (selectedLanguage == AppLanguage.AR) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.secondary
        }

        val englishBackgroundColor = if (selectedLanguage == AppLanguage.EN) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.secondary
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onLanguageSelected(AppLanguage.EN)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = englishBackgroundColor
            ),
            shape = RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp)
        ) {
            Text(text = stringResource(id = R.string.settings_items_language_options_english))
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onLanguageSelected(AppLanguage.AR)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = arabicBackgroundColor
            ),
            shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp)
        ) {
            Text(text = stringResource(id = R.string.settings_items_language_options_arabic))
        }

    }
}

@Preview(locale = "ar", showSystemUi = true)
@Composable
private fun SettingsScreenPreview() = PrayerCompanionAndroidTheme {
    SettingsScreen(SettingsState()) {}
}