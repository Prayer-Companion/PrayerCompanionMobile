package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.data.preferences.AppPreferences
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.utils.PermissionsManager
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionsRequestViewModel @Inject constructor(
    private val permissionsManager: PermissionsManager,
    private val dataStoresRepo: DataStoresRepo
) : ViewModel() {

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var uiState: PermissionsRequestUiState by mutableStateOf(PermissionsRequestUiState())
    private var currentState = State.Location

    private var appPreferences: AppPreferences? = null

    init {
        fun observeDataStore() {
            viewModelScope.launch {
                dataStoresRepo.appPreferencesDataStore.data.collect {
                    appPreferences = it
                }
            }
        }

        if (permissionsManager.isLocationPermissionGranted.not()) {
            setLocationPermissionState()
        } else if (permissionsManager.isPushNotificationAllowed.not()) {
            setNotificationPermissionState()
        } else {
            goToHomeScreen()
        }

        observeDataStore()
    }

    fun onEvent(event: PermissionsRequestEvent) {
        when (event) {
            is PermissionsRequestEvent.OnPermissionResult -> onPermissionResult(event.permissions)
            is PermissionsRequestEvent.OnSkipNotificationPermission -> onSkipNotificationPermission()
            is PermissionsRequestEvent.OnCTAClicked -> onCTAClicked()
        }
    }

    private fun onPermissionResult(permissions: Map<String, Boolean>) {
        viewModelScope.launch {
            //a small delay for dialog to be dismissed so we can show feedback/changes on the screen
            delay(500)
            when (currentState) {
                State.Location -> {
                    //if either approximate or precise permission were granted then we can proceed
                    if (permissions.any { it.value }) {
                        if (permissionsManager.isPushNotificationAllowed) {
                            goToHomeScreen()
                        } else {
                            nextState()
                        }
                    } else {
                        uiState = uiState
                            .copy(body = UiText.StringResource(R.string.location_permission_request_deny_body))

                        dataStoresRepo.appPreferencesDataStore.updateData {
                            val count = appPreferences?.deniedLocationPermissionsCount ?: 0
                            it.copy(
                                deniedLocationPermissionsCount = count + 1
                            )
                        }
                    }
                }

                State.Notification -> {
                    if (permissions.all { it.value }) {
                        goToHomeScreen()
                    } else {
                        uiState = uiState
                            .copy(body = UiText.StringResource(R.string.notification_permission_request_deny_body))

                        dataStoresRepo.appPreferencesDataStore.updateData {
                            val count = appPreferences?.deniedNotificationPermissionsCount ?: 0
                            it.copy(
                                deniedNotificationPermissionsCount = count + 1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSkipNotificationPermission() {
        goToHomeScreen()
        viewModelScope.launch {
            dataStoresRepo.appPreferencesDataStore.updateData {
                it.copy(hasSkippedNotificationPermission = true)
            }
        }
    }

    private fun onCTAClicked() {
        when (currentState) {
            State.Location -> {
                val isLocationPermissionGranted = permissionsManager.isLocationPermissionGranted
                if (isLocationPermissionGranted.not()) {
                    val denialCount = appPreferences?.deniedLocationPermissionsCount ?: 0
                    if (denialCount < 2) {
                        viewModelScope.launch {
                            _uiEvents.send(UiEvent.RequestPermissions(uiState.permissions))
                        }
                    } else {
                        viewModelScope.launch {
                            _uiEvents.send(UiEvent.OpenAppSettings)
                        }
                    }
                } else {
                    nextState()
                }
            }

            State.Notification -> {
                val isNotificationPermissionGranted = permissionsManager.isPushNotificationAllowed
                if (isNotificationPermissionGranted.not()) {
                    val denialCount = appPreferences?.deniedNotificationPermissionsCount ?: 0
                    if (denialCount < 2) {
                        viewModelScope.launch {
                            _uiEvents.send(UiEvent.RequestPermissions(uiState.permissions))
                        }
                    } else {
                        viewModelScope.launch {
                            _uiEvents.send(UiEvent.OpenAppSettings)
                        }
                    }
                } else {
                    nextState()
                }
            }
        }
    }

    private fun nextState() {
        when (currentState) {
            State.Location -> {
                if (permissionsManager.isPushNotificationAllowed) {
                    goToHomeScreen()
                } else {
                    setNotificationPermissionState()
                }
            }

            State.Notification -> goToHomeScreen()
        }
    }

    private fun setLocationPermissionState() {
        uiState = uiState.copy(
            icon = R.drawable.ic_location,
            title = UiText.StringResource(R.string.location_permission_request_title),
            body = UiText.StringResource(R.string.location_permission_request_explanation),
            ctaText = UiText.StringResource(R.string.location_permission_request_cta),
            permissions = PermissionsManager.locationPermissions,
            skippable = false
        )
        currentState = State.Location
    }

    private fun setNotificationPermissionState() {
        uiState = uiState.copy(
            icon = R.drawable.ic_location, // todo use notifications icon when provided
            title = UiText.StringResource(R.string.notification_permission_request_title),
            body = UiText.StringResource(R.string.notification_permission_request_explanation),
            ctaText = UiText.StringResource(R.string.notification_permission_request_cta),
            permissions = listOf(PermissionsManager.notificationsPermission),
            skippable = true
        )
        currentState = State.Notification
    }

    private fun goToHomeScreen() {
        viewModelScope.launch {
            _uiEvents.send(UiEvent.Navigate(Route.Home))
        }
    }

    enum class State {
        Location, Notification
    }

}