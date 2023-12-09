package com.prayercompanion.shared.presentation.features.main.settings

import com.prayercompanion.shared.domain.models.app.AppLanguage

sealed class SettingsEvent {
    data object OnStart: SettingsEvent()
    data object OnFeedbackBoxClicked: SettingsEvent()
    data class OnPauseMediaCheckedChange(val checked: Boolean) : SettingsEvent()
    data class OnLanguageSelected(val language: AppLanguage) : SettingsEvent()
}
