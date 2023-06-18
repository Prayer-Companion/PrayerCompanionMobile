package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.content.IntentSender
import android.os.CountDownTimer
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetCurrentPrayer
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetLastWeekStatusesOverView
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetNextPrayer
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.UpdatePrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import com.prayercompanion.prayercompanionandroid.presentation.utils.toUiText
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.asLog
import logcat.logcat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getDayPrayers: GetDayPrayers,
    private val getCurrentPrayer: GetCurrentPrayer,
    private val getNextPrayer: GetNextPrayer,
    private val updatePrayerStatus: UpdatePrayerStatus,
    private val getLastWeekStatusesOverView: GetLastWeekStatusesOverView,
    private val locationManager: AppLocationManager
) : ViewModel() {

    private var loadSelectedDatePrayersJob: Job? = null
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var state: HomeScreenState by mutableStateOf(HomeScreenState())
        private set
    var durationUntilNextPrayer by mutableStateOf(RemainingDuration(0, 0, 0))
        private set

    init {
        suspend fun loadCurrentAndNextPrayers() {
            suspend fun updateState(
                currentPrayer: PrayerInfo = state.currentPrayer,
                nextPrayer: PrayerInfo = state.nextPrayer
            ) = withContext(Dispatchers.Main) {
                state = state.copy(currentPrayer = currentPrayer, nextPrayer = nextPrayer)
            }

            val currentPrayerInfo = getCurrentPrayer.call().getOrElse {
                it.printStackTraceInDebug()
                sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                return
            }

            updateState(currentPrayer = currentPrayerInfo)

            val nextPrayerInfo = getNextPrayer.call(currentPrayerInfo).getOrElse {
                it.printStackTraceInDebug()
                sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                return
            }
            updateState(nextPrayer = nextPrayerInfo)
            withContext(Dispatchers.Main) { startDurationCountDown() }
        }

        suspend fun loadInitialDayPrayers() {
            withContext(Dispatchers.IO) {
                val dateDayPrayers = getDayPrayers.call(LocalDate.now(), true).getOrElse {
                    withContext(Dispatchers.Main) {
                        state = state.copy(selectedDayPrayersInfo = DayPrayersInfo.Default)
                        sendEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.message.toString())))
                    }
                    return@withContext
                }
                withContext(Dispatchers.Main) {
                    state = state.copy(selectedDayPrayersInfo = dateDayPrayers)
                }

            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            loadInitialDayPrayers()
            awaitAll(
                async {
                    loadCurrentAndNextPrayers()
                },
                async {
                    getLastWeekStatusesOverView.call()
                        .collectLatest { statuses ->
                            withContext(Dispatchers.Main) {
                                state = state.copy(lastWeekStatuses = statuses)
                            }
                        }
                }
            )

        }

        val locationServiceTask = locationManager.checkLocationService()
        locationServiceTask
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        val intentSenderRequest = IntentSenderRequest
                            .Builder(exception.resolution.intentSender)
                            .build()

                        sendEvent(UiEvent.LaunchIntentSenderRequest(intentSenderRequest))

                    } catch (sendEx: IntentSender.SendIntentException) {
                        logcat { sendEx.asLog() }
                    }
                }
            }
    }

    fun onPreviousDayButtonClicked() {
        state = state.copy(selectedDate = state.selectedDate.minusDays(1))
        loadSelectedDatePrayers()
    }

    fun onNextDayButtonClicked() {
        state = state.copy(selectedDate = state.selectedDate.plusDays(1))
        loadSelectedDatePrayers()
    }

    fun onStatusSelected(prayerStatus: PrayerStatus, prayerInfo: PrayerInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePrayerStatus.call(prayerInfo, prayerStatus).onFailure {
                it.printStackTraceInDebug()
                sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                return@launch
            }

            state = state.updateStatus(prayerInfo, prayerStatus)
        }
    }

    fun onLocationSettingsResult(result: Boolean) {
        if (result) {
            locationManager.getRequestLocationUpdates {
                loadSelectedDatePrayers(true)
            }
        }
    }

    private fun loadSelectedDatePrayers(forceUpdate: Boolean = false) {
        // TODO: should we block the Ui while loading instead of canceling and reloading again?
        if (loadSelectedDatePrayersJob?.isActive == true) {
            loadSelectedDatePrayersJob?.cancel()
        }

        val selectedDate = state.selectedDate
        loadSelectedDatePrayersJob = viewModelScope.launch(Dispatchers.IO) {
            getDayPrayers.call(selectedDate, forceUpdate)
                .onSuccess { dateDayPrayers ->
                    withContext(Dispatchers.Main) {
                        state = state.copy(selectedDayPrayersInfo = dateDayPrayers)
                    }
                }
                .onFailure {
                    withContext(Dispatchers.Main) {
                        state = state.copy(selectedDayPrayersInfo = DayPrayersInfo.Default)
                        sendEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.message.toString())))
                    }
                }
        }
    }

    private fun startDurationCountDown() {
        val durationInMillis = Duration
            .between(LocalDateTime.now(), state.nextPrayer.dateTime)
            .toMillis()

        durationUntilNextPrayer = RemainingDuration.fromMilliSeconds(durationInMillis)
        object : CountDownTimer(durationInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                durationUntilNextPrayer =
                    RemainingDuration.fromMilliSeconds(millisUntilFinished)
            }

            override fun onFinish() {
                moveToNextPrayer()
            }
        }.start()
    }

    private fun moveToNextPrayer() {
        viewModelScope.launch(Dispatchers.IO) {
            val nextPrayer = getNextPrayer.call(state.nextPrayer).getOrElse {
                it.printStackTraceInDebug()
                sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                return@launch
            }
            state = state.copy(currentPrayer = state.nextPrayer, nextPrayer = nextPrayer)
            withContext(Dispatchers.Main) { startDurationCountDown() }
        }
    }

    private fun sendErrorEvent(message: UiText) {
        sendEvent(
            UiEvent.ShowErrorSnackBar(message)
        )
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiEvents.send(event)
        }
    }
}