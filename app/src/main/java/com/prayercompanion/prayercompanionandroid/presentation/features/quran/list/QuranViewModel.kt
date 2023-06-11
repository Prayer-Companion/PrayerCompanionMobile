package com.prayercompanion.prayercompanionandroid.presentation.features.quran.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.AddMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.EditMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.GetFullQuranWithMemorized
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.GetNextQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.MarkQuranSectionAsRead
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.RemoveMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import com.prayercompanion.prayercompanionandroid.presentation.utils.toUiText
import com.prayercompanion.prayercompanionandroid.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    getFullQuranWithMemorized: GetFullQuranWithMemorized,
    private val addMemorizedChapterAyat: AddMemorizedChapterAyat,
    private val editMemorizedChapterAyat: EditMemorizedChapterAyat,
    private val removeMemorizedChapterAyat: RemoveMemorizedChapterAyat,
    private val getNextQuranReadingSections: GetNextQuranReadingSections,
    private val markQuranSectionAsRead: MarkQuranSectionAsRead
) : ViewModel() {

    var state: QuranState by mutableStateOf(QuranState())
        private set
    private val _uiEventsChannel = Channel<UiEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val quranResult = getFullQuranWithMemorized.call()
            quranResult.onSuccess { quran ->
                state = state.copy(quran = quran)
            }
        }
        getNextSections()
    }

    fun onEvent(event: QuranEvent) {
        when (event) {
            is QuranEvent.OnChapterSelected -> addMemorizedChapterAyat(
                event.chapterId,
                event.fromVerse,
                event.toVerse
            )

            is QuranEvent.OnChapterAyatSaved -> editMemorizedChapterAyat(
                event.chapterId,
                event.fromVerse,
                event.toVerse
            )

            is QuranEvent.OnChapterDeselected -> removeMemorizedChapterAyat(event.chapterId)
            QuranEvent.OnNextSectionClicked -> onNextSectionClicked()
            QuranEvent.OnViewFullClicked -> onViewFullClicked()
        }
    }

    private fun addMemorizedChapterAyat(chapterId: Int, fromVerse: Int, toVerse: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = addMemorizedChapterAyat.call(chapterId, fromVerse, toVerse)

            result.onSuccess {
                val quran = state.quran
                quran.chapters.firstOrNull { it.id == chapterId }?.apply {
                    isMemorized = true
                    memorizedFrom = fromVerse
                    memorizedTo = toVerse
                }

                state = state.copy(quran = quran)
            }.onFailure {
                sendUiEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.toString())))
            }
        }
    }

    private fun editMemorizedChapterAyat(chapterId: Int, fromVerse: Int, toVerse: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = editMemorizedChapterAyat.call(chapterId, fromVerse, toVerse)

            result.onSuccess {
                withContext(Dispatchers.Main) {
                    val quran = state.quran
                    quran.chapters.find { it.id == chapterId }?.apply {
                        isMemorized = true
                        memorizedFrom = fromVerse
                        memorizedTo = toVerse
                    }

                    state = state.copy(quran = quran)
                }
            }.onFailure {
                sendUiEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.toString())))
            }
        }
    }

    private fun removeMemorizedChapterAyat(chapterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = removeMemorizedChapterAyat.call(chapterId)

            result.onSuccess {
                val quran = state.quran
                val chapter = quran.chapters.firstOrNull { it.id == chapterId } ?: return@launch
                chapter.isMemorized = false
                state = state.copy(quran = quran)
            }.onFailure {
                sendUiEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.toString())))
            }
        }
    }

    private fun onNextSectionClicked() {
        state.sections?.let { sections ->
            viewModelScope.launch(Dispatchers.IO) {
                markQuranSectionAsRead.call(sections)
                getNextSections()
            }
        }
    }

    private fun onViewFullClicked() {
        val sections = state.sections ?: return
        sendUiEvent(
            UiEvent.Navigate(
                Route.FullQuranSections,
                listOf(toJson(sections))
            )
        )
    }

    private fun getNextSections() {
        viewModelScope.launch(Dispatchers.IO) {
            val sections = getNextQuranReadingSections.call()
            sections.onSuccess {
                state = state.copy(sections = it)
            }.onFailure {
                sendUiEvent(UiEvent.ShowErrorSnackBar(it.message.toUiText()))
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(uiEvent)
        }
    }
}