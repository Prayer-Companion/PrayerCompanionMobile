package com.prayercompanion.prayercompanionandroid.presentation.features.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.SetAppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val setAppLanguage: SetAppLanguage,
    getAppLanguage: GetAppLanguage
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    init {
        val language = getAppLanguage.call()
        state = state.copy(language = language ?: AppLanguage.AR)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnLanguageSelected -> onLanguageSelected(event.language)
        }
    }

    private fun onLanguageSelected(language: AppLanguage) {
        setAppLanguage.call(language)
        state = state.copy(language = language)
    }
}