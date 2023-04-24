package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.DayPrayersInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.models.RemainingDuration
import com.prayercompanion.prayercompanionandroid.domain.usecases.*
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
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
    private val appLocationManager: AppLocationManager
) : ViewModel() {

    private var loadCurrentDatePrayersJob: Job? = null
    private val _uiEventsChannel = Channel<UiEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    private var currentPrayer by mutableStateOf(PrayerInfo.Default)
    var currentDate: LocalDate by mutableStateOf(LocalDate.now())
        private set
    var dayPrayersInfo by mutableStateOf(DayPrayersInfo.Default)
        private set
    var nextPrayer by mutableStateOf(PrayerInfo.Default)
        private set
    var durationUntilNextPrayer by mutableStateOf(RemainingDuration(0, 0, 0))
        private set

    init {
        loadCurrentDatePrayers()
    }

    fun onPreviousDayButtonClicked() {
        currentDate = currentDate.minusDays(1)
        loadCurrentDatePrayers()
    }

    fun onNextDayButtonClicked() {
        currentDate = currentDate.plusDays(1)
        loadCurrentDatePrayers()
    }

    fun onStatusSelected(prayerStatus: PrayerStatus, prayerInfo: PrayerInfo) {
        viewModelScope.launch {
            val result = updatePrayerStatus.call(currentDate, prayerInfo, prayerStatus)
            result.onSuccess {
                currentPrayer.status = prayerStatus
            }.onFailure {
                sendEvent(UiEvent.ShowErrorSnackBar(UiText.StringResource(R.string.error_something_went_wrong)))
            }
        }
    }

    private fun loadCurrentDatePrayers() {
        if (loadCurrentDatePrayersJob?.isActive == true) {
            loadCurrentDatePrayersJob?.cancel()
        }

        appLocationManager.getLastKnownLocation { location ->
            location ?: return@getLastKnownLocation

            val currentDate = currentDate
            loadCurrentDatePrayersJob = viewModelScope.launch(Dispatchers.IO) {
                getDayPrayers.call(currentDate, location)
                    .onSuccess { dateDayPrayers ->
                        withContext(Dispatchers.Main) {
                            dayPrayersInfo = dateDayPrayers
                            updatePrayersAndStartCountDown()
                        }
                    }
                    .onFailure {
                        withContext(Dispatchers.Main) {
                            dayPrayersInfo = DayPrayersInfo.Default
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
                val currentPrayerResult = getCurrentPrayer.call(location)
                    .onSuccess {
                        currentPrayer = it
                    }

                val nextPrayerResult = getNextPrayer.call(location)
                    .onSuccess {
                        nextPrayer = it
                        withContext(Dispatchers.Main) {
                            startDurationCountDown()
                        }
                    }

                if (currentPrayerResult.isFailure || nextPrayerResult.isFailure) {
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
            _uiEventsChannel.send(event)
        }
    }
}