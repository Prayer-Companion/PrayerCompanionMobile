package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.data.utils.PrayersNotificationsService
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetCurrentPrayer
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetNextPrayer
import com.prayercompanion.prayercompanionandroid.domain.usecases.UpdatePrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import com.prayercompanion.prayercompanionandroid.printStackTraceInDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val appLocationManager: AppLocationManager,
    private val prayersNotificationsService: PrayersNotificationsService
) : ViewModel() {

    private var loadSelectedDatePrayersJob: Job? = null
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var currentPrayer by mutableStateOf(PrayerInfo.Default)
        private set
    var nextPrayer by mutableStateOf(PrayerInfo.Default)
        private set
    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
        private set
    var currentDayPrayersInfo by mutableStateOf(DayPrayersInfo.Default)
        private set
    var selectedDayPrayersInfo by mutableStateOf(DayPrayersInfo.Default)
        private set
    var durationUntilNextPrayer by mutableStateOf(RemainingDuration(0, 0, 0))
        private set

    init {
        loadSelectedDatePrayers(true)
    }

    fun onPreviousDayButtonClicked() {
        selectedDate = selectedDate.minusDays(1)
        loadSelectedDatePrayers()
    }

    fun onNextDayButtonClicked() {
        selectedDate = selectedDate.plusDays(1)
        loadSelectedDatePrayers()
    }

    fun onStatusSelected(prayerStatus: PrayerStatus, prayerInfo: PrayerInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = updatePrayerStatus.call(prayerInfo, prayerStatus)
            result.onSuccess {
                currentPrayer.status = prayerStatus
                selectedDayPrayersInfo.get(prayerInfo.prayer).status = prayerStatus
                selectedDayPrayersInfo = selectedDayPrayersInfo
            }.onFailure {
                sendEvent(UiEvent.ShowErrorSnackBar(UiText.StringResource(R.string.error_something_went_wrong)))
            }
        }
    }

    private fun loadSelectedDatePrayers(forceUpdate: Boolean = false) {
        // TODO: we should block the Ui while loading instead of canceling and reloading again
        if (loadSelectedDatePrayersJob?.isActive == true) {
            loadSelectedDatePrayersJob?.cancel()
        }

        appLocationManager.getLastKnownLocation { location ->
            location ?: return@getLastKnownLocation

            val currentDate = selectedDate
            loadSelectedDatePrayersJob = viewModelScope.launch(Dispatchers.IO) {
                getDayPrayers.call(currentDate, location, forceUpdate)
                    .onSuccess { dateDayPrayers ->
                        withContext(Dispatchers.Main) {
                            if (currentDate == LocalDate.now()) {
                                currentDayPrayersInfo = dateDayPrayers
                            }
                            selectedDayPrayersInfo = dateDayPrayers
                            updatePrayersAndStartCountDown()
                        }
                    }
                    .onFailure {
                        withContext(Dispatchers.Main) {
                            selectedDayPrayersInfo = DayPrayersInfo.Default
                            sendEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.message.toString())))
                        }
                    }
            }
        }
    }

    private fun updatePrayersAndStartCountDown() {
        appLocationManager.getLastKnownLocation { location ->
            location ?: return@getLastKnownLocation
            viewModelScope.launch(Dispatchers.IO) {
                // TODO: Create app's readable exceptions
                getCurrentPrayer.call(currentDayPrayersInfo, location)
                    .onSuccess {
                        currentPrayer = it
                    }
                    .onFailure {
                        it.printStackTraceInDebug()
                        sendEvent(UiEvent.ShowErrorSnackBar(UiText.StringResource(R.string.error_something_went_wrong)))
                    }

                getNextPrayer.call(currentDayPrayersInfo, location)
                    .onSuccess {
                        nextPrayer = it
                        withContext(Dispatchers.Main) {
                            startDurationCountDown()
                        }
                    }
                    .onFailure {
                        it.printStackTraceInDebug()
                        sendEvent(UiEvent.ShowErrorSnackBar(UiText.StringResource(R.string.error_something_went_wrong)))
                    }
            }
        }
    }

    private fun startDurationCountDown() {
        val durationInMillis = Duration.between(LocalDateTime.now(), nextPrayer.dateTime).toMillis()
        durationUntilNextPrayer = RemainingDuration.fromMilliSeconds(durationInMillis)
        object : CountDownTimer(durationInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                durationUntilNextPrayer = RemainingDuration.fromMilliSeconds(millisUntilFinished)
            }

            override fun onFinish() {
                updatePrayersAndStartCountDown()
            }
        }.start()
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiEvents.send(event)
        }
    }
}