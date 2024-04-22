package com.prayercompanion.shared.presentation.features.main.settings

import com.prayercompanion.shared.domain.models.app.AppLanguage

data class SettingsState(
    val language: AppLanguage = AppLanguage.AR,
    val isPauseMediaPreferencesEnabled: Boolean = true
)
