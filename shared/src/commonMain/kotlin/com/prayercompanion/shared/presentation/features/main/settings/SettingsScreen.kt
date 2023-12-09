package com.prayercompanion.shared.presentation.features.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.prayercompanion.shared.BottomNavItem
import com.prayercompanion.shared.domain.models.app.AppLanguage
import com.prayercompanion.shared.presentation.components.AppBackground
import com.prayercompanion.shared.presentation.components.TitleHeader
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.createTabOptions
import com.prayercompanion.shared.presentation.utils.stringResource
import kotlinx.coroutines.flow.Flow

object SettingsScreen : Tab {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SettingsScreenViewModel>()
        SettingsScreen(
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            uiEvents = viewModel.uiEvents
        )
    }

    override val options: TabOptions
        @Composable
        get() = createTabOptions(BottomNavItem.Settings)
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    uiEvents: Flow<UiEvent>,
) {
    val spacing = LocalSpacing.current

    LaunchedEffect(key1 = true) {
        onEvent(SettingsEvent.OnStart)
    }

    LaunchedEffect(key1 = Unit) {
        uiEvents.collect {
            when (it) {
                is UiEvent.ShowFeedbackDialog -> {
//                    showFeedbackDialog() todo kmp
                }

                else -> Unit
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground()
        Column(modifier = Modifier.fillMaxSize()) {
            TitleHeader(
                title = stringResource(id = StringRes.settings_title)
            )
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            SettingsSection(
                modifier = Modifier.padding(horizontal = spacing.spaceLarge),
                title = stringResource(id = StringRes.settings_items_language)
            ) {
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
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            SettingsSection(
                modifier = Modifier.padding(horizontal = spacing.spaceLarge),
                title = stringResource(id = StringRes.settings_items_preferences)
            ) {
                SettingsToggle(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = StringRes.settings_items_preferences_pauseMedia),
                    isChecked = state.isPauseMediaPreferencesEnabled,
                    onCheckedChange = {
                        onEvent(SettingsEvent.OnPauseMediaCheckedChange(it))
                    },
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            SettingsSection(
                modifier = Modifier.padding(horizontal = spacing.spaceLarge),
                title = stringResource(id = StringRes.settings_items_feedback_title)
            ) {
                FeedbackBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    onEvent(SettingsEvent.OnFeedbackBoxClicked)
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    val spacing = LocalSpacing.current

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
        content()
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
            Text(text = stringResource(id = StringRes.settings_items_language_options_english))
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
            Text(text = stringResource(id = StringRes.settings_items_language_options_arabic))
        }

    }
}

@Composable
private fun SettingsToggle(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = spacing.spaceMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
        Switch(
            modifier = Modifier.scale(1.05f),
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.onPrimary,
                checkedTrackColor = MaterialTheme.colors.primaryVariant,
                checkedTrackAlpha = 1f,
                uncheckedThumbColor = MaterialTheme.colors.onPrimary,
            ),
        )
    }
}

@Composable
private fun FeedbackBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Text(
        modifier = modifier
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colors.onPrimary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(spacing.spaceSmall)
            .padding(bottom = spacing.spaceMedium),
        text = stringResource(id = StringRes.settings_items_feedback_prompt),
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.secondary
    )
}