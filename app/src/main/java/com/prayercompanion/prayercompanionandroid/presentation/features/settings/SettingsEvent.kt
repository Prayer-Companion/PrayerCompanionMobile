package com.prayercompanion.prayercompanionandroid.presentation.features.settings

import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage

sealed class SettingsEvent {
    data class OnLanguageSelected(val language: AppLanguage) : SettingsEvent()
}
