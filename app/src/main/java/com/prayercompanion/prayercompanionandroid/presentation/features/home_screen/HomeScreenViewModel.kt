package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.extensions.instantBetween
import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.models.DayPrayersInfo
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.models.PrayerStatus
import com.prayercompanion.shared.domain.usecases.prayers.GetDailyPrayersCombo
import com.prayercompanion.shared.domain.usecases.prayers.GetDayPrayersFlow
import com.prayercompanion.shared.domain.usecases.prayers.GetStatusesOverView
import com.prayercompanion.shared.domain.usecases.prayers.SetPrayerStatusByDateTime
import com.prayercompanion.shared.domain.usecases.prayers.UpdatePrayerStatus
import com.prayercompanion.shared.domain.usecases.quran.LoadAndSaveQuranMemorizedChapters
import com.prayercompanion.shared.domain.utils.AppLocationManager
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.presentation.models.RemainingDuration
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.UiText
import com.prayercompanion.shared.presentation.utils.printStackTraceInDebug
import com.prayercompanion.shared.presentation.utils.toUiText
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class HomeScreenViewModel constructor(
    private val getDayPrayersFlow: GetDayPrayersFlow,
    private val updatePrayerStatus: UpdatePrayerStatus,
    private val getStatusesOverView: GetStatusesOverView,
    private val locationManager: AppLocationManager,
    private val loadAndSaveQuranMemorizedChapters: LoadAndSaveQuranMemorizedChapters,
    private val getDailyPrayersCombo: GetDailyPrayersCombo,
    private val setPrayerStatusByDateTime: SetPrayerStatusByDateTime,
    private val tracker: Tracker,
    private val dataStoresRepo: DataStoresRepo
) : ViewModel() {

    private val appPreferencesData = dataStoresRepo.appPreferencesDataStoreData

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

        loadQuranData()
        loadDailyPrayersCombo()
        loadStatusesOverView()
    }

    fun onStart() {
        state = state.copy(selectedDate = LocalDate.now())
        loadDailyPrayersCombo()
    }

    fun onPause() {
        lastForegroundTime = System.currentTimeMillis()
    }

    fun onPreviousDayButtonClicked() {
        tracker.trackButtonClicked(TrackedButtons.VIEW_PREVIOUS_DAY_PRAYERS)
        updateSelectedDate(state.selectedDate.minus(1, DateTimeUnit.DAY))
    }

    fun onNextDayButtonClicked() {
        tracker.trackButtonClicked(TrackedButtons.VIEW_NEXT_DAY_PRAYERS)
        updateSelectedDate(state.selectedDate.plus(1, DateTimeUnit.DAY))
    }

    fun onStatusSelected(prayerStatus: PrayerStatus, prayerInfo: PrayerInfo) {
        tracker.trackStatusSelect()
        viewModelScope.launch(Dispatchers.IO) {
            updatePrayerStatus.call(prayerInfo, prayerStatus)
                .onSuccess {
                    if (appPreferencesData.first().hasShownRateTheAppPopup.not()) {
                        sendEvent(UiEvent.ShowRateTheAppPopup)
                        dataStoresRepo.updateAppPreferencesDataStore {
                            it.copy(hasShownRateTheAppPopup = true)
                        }
                    }
                }
                .onFailure {
                    it.printStackTraceInDebug()
                    sendErrorEvent(StringRes.error_something_went_wrong.toUiText())
                    return@launch
                }
        }
    }

    fun onIshaStatusesPeriodsExplanationClicked() {
        tracker.trackButtonClicked(TrackedButtons.ISHA_STATUSES_PERIOD_EXPLANATION)
        sendEvent(UiEvent.OpenWebUrl(BuildConfig.ISHA_STATUSES_PERIODS_EXPLANATION_URL))
    }

    fun onStatusOverviewBarClicked() {
        tracker.trackButtonClicked(TrackedButtons.STATUS_OVER_VIEW_BAR)
    }

    fun onPrayedNowClicked() {
        tracker.trackButtonClicked(TrackedButtons.HOME_PRAYED_NOW)
        viewModelScope.launch(Dispatchers.IO) {
            setPrayerStatusByDateTime
                .call(headerState.currentAndNextPrayer.first, LocalDateTime.now())
                .onFailure {
                    it.printStackTraceInDebug()
                    sendErrorEvent(StringRes.error_something_went_wrong.toUiText())
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
                    it.printStackTraceInDebug()
                    sendErrorEvent(UiText.DynamicString(it.message.toString()))
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
                            it.printStackTraceInDebug()
                            state = state.copy(selectedDayPrayersInfo = DayPrayersInfo.Default)
                            sendErrorEvent(UiText.DynamicString(it.message.toString()))
                        }
                    }
                }
        }
    }

    private fun startDurationCountDown() {
        val durationInMillis = instantBetween(
            LocalDateTime.now(),
            headerState.currentAndNextPrayer.second.dateTime
        ).inWholeMilliseconds

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