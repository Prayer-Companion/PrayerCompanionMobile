package com.prayercompanion.prayercompanionandroid.presentation.features.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.SetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.settings.GetIsPauseMediaEnabled
import com.prayercompanion.prayercompanionandroid.domain.usecases.settings.SetPauseMediaEnabled
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val setAppLanguage: SetAppLanguage,
    private val getAppLanguage: GetAppLanguage,
    private val setPauseMediaEnabled: SetPauseMediaEnabled,
    private val getIsPauseMediaEnabled: GetIsPauseMediaEnabled,
) : ViewModel() {

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()
    var state by mutableStateOf(SettingsState())
        private set

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnStart -> onStart()
            is SettingsEvent.OnLanguageSelected -> onLanguageSelected(event.language)
            is SettingsEvent.OnPauseMediaCheckedChange -> onPauseMediaCheckedChange(event.checked)
            is SettingsEvent.OnFeedbackBoxClicked -> sendEvent(UiEvent.ShowFeedbackDialog)
        }
    }

    private fun onStart() {
        viewModelScope.launch {
            val language = getAppLanguage.call()
            val isPauseMediaEnabled = getIsPauseMediaEnabled.call()
            state = state.copy(
                language = language,
                isPauseMediaPreferencesEnabled = isPauseMediaEnabled
            )
        }
    }

    private fun onLanguageSelected(language: AppLanguage) {
        setAppLanguage.call(language)
        state = state.copy(language = language)
    }

    private fun onPauseMediaCheckedChange(checked: Boolean) {
        viewModelScope.launch {
            setPauseMediaEnabled.call(checked)
            state = state.copy(isPauseMediaPreferencesEnabled = checked)
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiEvents.send(event)
        }
    }
}