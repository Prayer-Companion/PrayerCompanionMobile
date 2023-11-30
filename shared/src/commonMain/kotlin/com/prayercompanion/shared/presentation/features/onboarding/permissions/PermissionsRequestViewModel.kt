package com.prayercompanion.shared.presentation.features.onboarding.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.prayercompanion.shared.data.preferences.AppPreferences
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.utils.MokoPermissionsManager
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.UiText
import dev.icerock.moko.permissions.Permission
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PermissionsRequestViewModel constructor(
    private val dataStoresRepo: DataStoresRepo,
    private val tracker: Tracker
) : ScreenModel {

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var uiState: PermissionsRequestUiState by mutableStateOf(PermissionsRequestUiState())
    private var currentState = State.Location

    private var appPreferences: AppPreferences? = null
    private var isLocationPermissionGranted = false
    private var isPushNotificationAllowed = false

    fun onEvent(event: PermissionsRequestEvent) {
        when (event) {
            is PermissionsRequestEvent.OnStart -> onStart(event.isLocationPermissionGranted, event.isPushNotificationAllowed)
            is PermissionsRequestEvent.OnPermissionResult -> onPermissionResult(event.permissions)
            is PermissionsRequestEvent.OnSkipNotificationPermission -> onSkipNotificationPermission()
            is PermissionsRequestEvent.OnCTAClicked -> onCTAClicked()
        }
    }

    private fun onStart(isLocationPermissionGranted: Boolean, isPushNotificationAllowed: Boolean) {
        this.isLocationPermissionGranted = isLocationPermissionGranted
        this.isPushNotificationAllowed = isPushNotificationAllowed
        fun observeDataStore() {
            screenModelScope.launch {
                dataStoresRepo.appPreferencesDataStoreData.collect {
                    appPreferences = it
                }
            }
        }

        if (isLocationPermissionGranted.not()) {
            setLocationPermissionState()
        } else if (isPushNotificationAllowed.not()) {
            setNotificationPermissionState()
        } else {
            goToHomeScreen()
        }

        observeDataStore()
    }

    private fun onPermissionResult(permissions: Map<Permission, Boolean>) {
        screenModelScope.launch {
            //a small delay for dialog to be dismissed so we can show feedback/changes on the screen
            delay(500)
            when (currentState) {
                State.Location -> {
                    //if either approximate or precise permission were granted then we can proceed
                    val granted = permissions.any { it.value }
                    tracker.trackLocationPermissionResult(granted)
                    if (granted) {
                        if (isPushNotificationAllowed) {
                            goToHomeScreen()
                        } else {
                            nextState()
                        }
                    } else {
                        uiState = uiState
                            .copy(body = UiText.StringResource(StringRes.location_permission_request_deny_body))

                        dataStoresRepo.updateAppPreferencesDataStore {
                            val count = appPreferences?.deniedLocationPermissionsCount ?: 0
                            it.copy(
                                deniedLocationPermissionsCount = count + 1
                            )
                        }
                    }
                }

                State.Notification -> {
                    val granted = permissions.all { it.value }
                    tracker.trackNotificationPermissionResult(granted)
                    if (granted) {
                        goToHomeScreen()
                    } else {
                        uiState = uiState
                            .copy(body = UiText.StringResource(StringRes.notification_permission_request_deny_body))

                        dataStoresRepo.updateAppPreferencesDataStore {
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
        tracker.trackButtonClicked(TrackedButtons.NOTIFICATION_PERMISSION_SKIP)
        goToHomeScreen()
        screenModelScope.launch {
            dataStoresRepo.updateAppPreferencesDataStore {
                it.copy(hasSkippedNotificationPermission = true)
            }
        }
    }

    private fun onCTAClicked() {
        when (currentState) {
            State.Location -> {
                if (isLocationPermissionGranted.not()) {
                    val denialCount = appPreferences?.deniedLocationPermissionsCount ?: 0
                    if (denialCount < 2) {
                        screenModelScope.launch {
                            _uiEvents.send(UiEvent.RequestPermissions(uiState.permissions))
                        }
                    } else {
                        screenModelScope.launch {
                            _uiEvents.send(UiEvent.OpenAppSettings)
                        }
                    }
                } else {
                    nextState()
                }
            }

            State.Notification -> {
                if (isPushNotificationAllowed.not()) {
                    val denialCount = appPreferences?.deniedNotificationPermissionsCount ?: 0
                    if (denialCount < 2) {
                        screenModelScope.launch {
                            _uiEvents.send(UiEvent.RequestPermissions(uiState.permissions))
                        }
                    } else {
                        screenModelScope.launch {
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
                if (isPushNotificationAllowed) {
                    goToHomeScreen()
                } else {
                    setNotificationPermissionState()
                }
            }

            State.Notification -> goToHomeScreen()
        }
    }

    private fun setLocationPermissionState() {
        //todo delegate those values to the view, and pass only a state of current permission
        uiState = uiState.copy(
            icon = "ic_location.xml",
            title = UiText.StringResource(StringRes.location_permission_request_title),
            body = UiText.StringResource(StringRes.location_permission_request_explanation),
            ctaText = UiText.StringResource(StringRes.location_permission_request_cta),
            permissions = MokoPermissionsManager.locationPermissions,
            skippable = false
        )
        currentState = State.Location
    }

    private fun setNotificationPermissionState() {
        uiState = uiState.copy(
            icon = "ic_location.xml", // todo use notifications icon
            title = UiText.StringResource(StringRes.notification_permission_request_title),
            body = UiText.StringResource(StringRes.notification_permission_request_explanation),
            ctaText = UiText.StringResource(StringRes.notification_permission_request_cta),
            permissions = listOf(MokoPermissionsManager.notificationsPermission),
            skippable = true
        )
        currentState = State.Notification
    }

    private fun goToHomeScreen() {
        screenModelScope.launch {
            _uiEvents.send(UiEvent.Navigate(Route.Home))
        }
    }

    enum class State {
        Location, Notification
    }

}