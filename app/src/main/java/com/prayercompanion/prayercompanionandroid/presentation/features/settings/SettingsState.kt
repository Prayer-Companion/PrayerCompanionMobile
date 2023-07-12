package com.prayercompanion.prayercompanionandroid.presentation.features.settings

import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage

data class SettingsState(
    val language: AppLanguage = AppLanguage.AR,
    val isPauseMediaPreferencesEnabled: Boolean = true
)
