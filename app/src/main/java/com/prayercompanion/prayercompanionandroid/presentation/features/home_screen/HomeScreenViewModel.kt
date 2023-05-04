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
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetCurrentPrayer
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetNextPrayer
import com.prayercompanion.prayercompanionandroid.domain.usecases.UpdatePrayerStatus
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getDayPrayers: GetDayPrayers,
    private val getCurrentPrayer: GetCurrentPrayer,
    private val getNextPrayer: GetNextPrayer,
    private val updatePrayerStatus: UpdatePrayerStatus
) : ViewModel() {

    private var loadSelectedDatePrayersJob: Job? = null
    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var state: HomeScreenState by mutableStateOf(HomeScreenState())
        private set
    var durationUntilNextPrayer by mutableStateOf(RemainingDuration(0, 0, 0))
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            awaitAll(
                async {
                    val currentPrayerInfo = getCurrentPrayer.call().getOrElse {
                        sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                        it.printStackTraceInDebug()
                        state = state.copy(currentPrayer = PrayerInfo.ErrorValue, nextPrayer = PrayerInfo.ErrorValue)
                        return@async
                    }
                    state = state.copy(currentPrayer = currentPrayerInfo)

                    val nextPrayerInfo = getNextPrayer.call(currentPrayerInfo).getOrElse {
                        it.printStackTraceInDebug()
                        sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                        it.printStackTraceInDebug()
                        state = state.copy(nextPrayer = PrayerInfo.ErrorValue)
                        return@async
                    }
                    state = state.copy(nextPrayer = nextPrayerInfo)

                    startDurationCountDown()
                },
                async {
                    loadSelectedDatePrayers(true)
                }
            )
        }
    }

    fun onPreviousDayButtonClicked() {
        state = state.copy(selectedDate = state.selectedDate.minusDays(1))
        viewModelScope.launch(Dispatchers.IO) {
            loadSelectedDatePrayers()
        }
    }

    fun onNextDayButtonClicked() {
        state = state.copy(selectedDate = state.selectedDate.plusDays(1))
        viewModelScope.launch(Dispatchers.IO) {
            loadSelectedDatePrayers()
        }
    }

    fun onStatusSelected(prayerStatus: PrayerStatus, prayerInfo: PrayerInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = updatePrayerStatus.call(prayerInfo, prayerStatus)
            result.onSuccess {
                state = state.updateStatus(prayerInfo, prayerStatus)
            }.onFailure {
                sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                it.printStackTraceInDebug()
            }
        }
    }

    private suspend fun loadSelectedDatePrayers(forceUpdate: Boolean = false) {
        /// TODO: check this coroutine
        coroutineScope {
            // TODO: should we block the Ui while loading instead of canceling and reloading again?
            if (loadSelectedDatePrayersJob?.isActive == true) {
                loadSelectedDatePrayersJob?.cancel()
            }

            loadSelectedDatePrayersJob = launch(Dispatchers.IO) {
                val selectedDate = withContext(Dispatchers.Main) {
                    state.selectedDate
                }
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
    }

    private suspend fun startDurationCountDown() {
        withContext(Dispatchers.Main) {
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
    }

    private fun moveToNextPrayer() {
        viewModelScope.launch(Dispatchers.IO) {
            val nextPrayer = getNextPrayer.call(state.nextPrayer).getOrElse{
                sendErrorEvent(R.string.error_something_went_wrong.toUiText())
                it.printStackTraceInDebug()
                return@launch
            }
            state = state.copy(currentPrayer = state.nextPrayer, nextPrayer = nextPrayer)
            startDurationCountDown()
        }
    }

    private fun sendErrorEvent(message: UiText) {
        sendEvent(
            UiEvent.ShowErrorSnackBar(
                UiText.DynamicString(message.toString())
            )
        )
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            _uiEvents.send(event)
        }
    }
}