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
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDailyPrayersCombo
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDayPrayersFlow
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetStatusesOverView
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.SetPrayerStatusByDateTime
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.asLog
import logcat.logcat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getDayPrayersFlow: GetDayPrayersFlow,
    private val updatePrayerStatus: UpdatePrayerStatus,
    private val getStatusesOverView: GetStatusesOverView,
    private val locationManager: AppLocationManager,
    private val loadAndSaveQuranMemorizedChapters: LoadAndSaveQuranMemorizedChapters,
    private val getDailyPrayersCombo: GetDailyPrayersCombo,
    private val setPrayerStatusByDateTime: SetPrayerStatusByDateTime,
    dataStoresRepo: DataStoresRepo
) : ViewModel() {

    private val appPreferences = dataStoresRepo.appPreferencesDataStore

    private var loadDailyPrayersComboJob: Job? = null
    private var loadSelectedDatePrayersJob: Job? = null
    private var statusesOverviewJob: Job? = null

    private var countDownTimer: CountDownTimer? = null
    private var lastForegroundTime = System.currentTimeMillis()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()
    var headerState: HomeScreenHeaderState by mutableStateOf(HomeScreenHeaderState())
        private set
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
        loadDailyPrayersCombo()
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
            updatePrayerStatus.call(prayerInfo, prayerStatus)
                .onSuccess {
                    if (appPreferences.data.first().hasShownRateTheAppPopup.not()) {
                        sendEvent(UiEvent.ShowRateTheAppPopup)
                        appPreferences.updateData {
                            it.copy(hasShownRateTheAppPopup = true)
                        }
                    }
                }
                .onFailure {
                    it.printStackTraceInDebug()
                    sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                    return@launch
                }
        }
    }

    fun onPrayedNowClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            setPrayerStatusByDateTime
                .call(headerState.currentAndNextPrayer.first, LocalDateTime.now())
                .onFailure {
                    it.printStackTraceInDebug()
                    sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                    return@launch
                }
        }
    }

    fun onLocationSettingsResult(result: Boolean) {
        if (result) {
            locationManager.getRequestLocationUpdates {
                loadDailyPrayersCombo()
            }
        }
    }

    private fun loadStatusesOverView() {
        statusesOverviewJob?.cancel()
        statusesOverviewJob = viewModelScope.launch(Dispatchers.IO) {
            getStatusesOverView.call()
                .collectLatest { statuses ->
                    withContext(Dispatchers.Main) {
                        statuses.remove(PrayerStatus.None)
                        headerState = headerState.copy(statusesOverview = statuses)
                    }
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
                .onCompletion {
                    loadSelectedDatePrayers()
                }
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        if (state.selectedDate == headerState.todayDate) {
                            headerState = headerState.copy(dailyPrayersCombo = it)
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
            getDayPrayersFlow.call(selectedDate)
                .collectLatest {
                    it.onSuccess { dateDayPrayers ->
                        withContext(Dispatchers.Main) {
                            state = state.copy(selectedDayPrayersInfo = dateDayPrayers)
                        }
                    }.onFailure {
                        withContext(Dispatchers.Main) {
                            state = state.copy(selectedDayPrayersInfo = DayPrayersInfo.Default)
                            sendErrorEvent(UiText.DynamicString(it.message.toString()))
                        }
                    }
                }
        }
    }

    private fun startDurationCountDown() {
        val durationInMillis = ChronoUnit.MILLIS
            .between(LocalDateTime.now(), headerState.currentAndNextPrayer.second.dateTime)

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
                loadStatusesOverView()
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
}