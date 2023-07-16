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
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.domain.usecases.IsConnectedToInternet
import com.prayercompanion.prayercompanionandroid.domain.usecases.UpdateAuthToken
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDailyPrayersCombo
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetLastWeekStatusesOverView
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.UpdatePrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.LoadAndSaveQuranMemorizedChapters
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
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
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
    private val updatePrayerStatus: UpdatePrayerStatus,
    private val getLastWeekStatusesOverView: GetLastWeekStatusesOverView,
    private val locationManager: AppLocationManager,
    private val updateAuthToken: UpdateAuthToken,
    private val loadAndSaveQuranMemorizedChapters: LoadAndSaveQuranMemorizedChapters,
    private val getDailyPrayersCombo: GetDailyPrayersCombo,
    private val isConnectedToInternet: IsConnectedToInternet
) : ViewModel() {

    private var loadDailyPrayersComboJob: Job? = null
    private var loadSelectedDatePrayersJob: Job? = null
    private var countDownTimer: CountDownTimer? = null
    private var lastForegroundTime = System.currentTimeMillis()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()
    var state: HomeScreenState by mutableStateOf(HomeScreenState())
        private set
    var durationUntilNextPrayer by mutableStateOf(RemainingDuration(0, 0, 0))
        private set

    init {
        fun loadQuranData() {
            viewModelScope.launch(Dispatchers.IO) {
                loadAndSaveQuranMemorizedChapters.call()
            }
        }

        fun loadStatusesOverView() {
            viewModelScope.launch(Dispatchers.IO) {
                awaitAll(
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
        }

        fun checkLocationService() {
            val locationServiceTask = locationManager.checkLocationService()
            locationServiceTask.addOnFailureListener { exception ->
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

        loadQuranData()
        loadDailyPrayersCombo()
        loadStatusesOverView()
        checkLocationService()
    }

    fun onStart() {
        val currentTime = System.currentTimeMillis()
        val durationSinceLastForegroundTime = currentTime - lastForegroundTime

        val hasInternet = isConnectedToInternet.call()
        val lastAuthTokenUpdateTime = Consts.userTokenUpdateTime
        val durationSinceLastUpdate = Duration
            .between(LocalDateTime.now(), lastAuthTokenUpdateTime ?: LocalDateTime.now())
            .toMillis()

        if (
            hasInternet &&
            (lastAuthTokenUpdateTime == null ||
                durationSinceLastUpdate > Consts.TOKEN_UPDATE_THRESHOLD_TIME_MS)
        ) {
            updateAuthToken.call(
                forceRefresh = true,
                onSuccess = {
                    updateSelectedDate(LocalDate.now())
                }
            )
        } else if (durationSinceLastForegroundTime > DURATION_AFTER_FOREGROUND_THRESHOLD_REFRESH_MS) {
            updateSelectedDate(LocalDate.now())
        }
    }

    fun onPause() {
        lastForegroundTime = System.currentTimeMillis()
    }

    fun onPreviousDayButtonClicked() {
        updateSelectedDate(state.selectedDate.minusDays(1))
    }

    fun onNextDayButtonClicked() {
        updateSelectedDate(state.selectedDate.plusDays(1))
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
                loadDailyPrayersCombo()
            }
        }
    }

    private fun loadDailyPrayersCombo() {
        if (loadDailyPrayersComboJob?.isActive == true) {
            loadDailyPrayersComboJob?.cancel()
        }

        loadDailyPrayersComboJob = viewModelScope.launch(Dispatchers.IO) {
            getDailyPrayersCombo.call()
                .cancellable()
                .catch {
                    sendErrorEvent(UiText.DynamicString(it.message.toString()))
                    logcat { it.asLog() }
                }
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        if (state.selectedDate == LocalDate.now()) {
                            state = state.copy(
                                dailyPrayersCombo = it,
                                selectedDayPrayersInfo = it.todayPrayersInfo
                            )
                        }
                        startDurationCountDown()
                    }
                }
        }
    }

    private fun updateSelectedDate(date: LocalDate) {
        state = state.copy(selectedDate = date)
        loadSelectedDatePrayers()
    }

    private fun loadSelectedDatePrayers() {
        if (loadSelectedDatePrayersJob?.isActive == true) {
            loadSelectedDatePrayersJob?.cancel()
        }

        val selectedDate = state.selectedDate
        loadSelectedDatePrayersJob = viewModelScope.launch(Dispatchers.IO) {
            getDayPrayers.call(selectedDate)
                .onSuccess { dateDayPrayers ->
                    withContext(Dispatchers.Main) {
                        state = state.copy(selectedDayPrayersInfo = dateDayPrayers)
                    }
                }
                .onFailure {
                    withContext(Dispatchers.Main) {
                        state = state.copy(selectedDayPrayersInfo = DayPrayersInfo.Default)
                        sendErrorEvent(UiText.DynamicString(it.message.toString()))
                    }
                }
        }
    }

    private fun startDurationCountDown() {
        val durationInMillis = Duration
            .between(LocalDateTime.now(), state.currentAndNextPrayer.second.dateTime)
            .toMillis()

        if (durationInMillis <= 0) return

        durationUntilNextPrayer = RemainingDuration.fromMilliSeconds(durationInMillis)
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                durationUntilNextPrayer =
                    RemainingDuration.fromMilliSeconds(millisUntilFinished)
            }

            override fun onFinish() {
                startDurationCountDown()
            }
        }.start()
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

    companion object {
        private const val DURATION_AFTER_FOREGROUND_THRESHOLD_REFRESH_MS = 5 * 60 * 1000 //5 min
    }
}