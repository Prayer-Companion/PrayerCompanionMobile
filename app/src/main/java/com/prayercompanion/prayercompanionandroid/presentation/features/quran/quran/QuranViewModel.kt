package com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.AddMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.EditMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.GetFullQuranWithMemorized
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.GetNextQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.LoadQuranReadingSuggestions
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.MarkQuranSectionAsRead
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.RemoveMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.TrackedButtons
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.Tracker
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiText
import com.prayercompanion.prayercompanionandroid.toJson

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuranViewModel constructor(
    getFullQuranWithMemorized: GetFullQuranWithMemorized,
    private val addMemorizedChapterAyat: AddMemorizedChapterAyat,
    private val editMemorizedChapterAyat: EditMemorizedChapterAyat,
    private val removeMemorizedChapterAyat: RemoveMemorizedChapterAyat,
    private val getNextQuranReadingSectionsFlow: GetNextQuranReadingSections,
    private val loadQuranReadingSuggestions: LoadQuranReadingSuggestions,
    private val markQuranSectionAsRead: MarkQuranSectionAsRead,
    private val tracker: Tracker
) : ViewModel() {

    private var hasChaptersBeenSorted = false
    private var chaptersOrder = listOf<Int>()
    var state: QuranState by mutableStateOf(QuranState())
        private set
    private val _uiEventsChannel = Channel<UiEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getFullQuranWithMemorized.call()
                    .collectLatest { quranResult ->
                        quranResult.onSuccess { quranChapters ->
                            withContext(Dispatchers.Main) {
                                if (hasChaptersBeenSorted.not()) {
                                    state.quranChapters = quranChapters.sortedBy { it.id }
                                        .sortedByDescending { it.isMemorized }
                                        .also {
                                            chaptersOrder = it.map { chapter ->
                                                chapter.id
                                            }
                                        }
                                    hasChaptersBeenSorted = true
                                } else {
                                    state.quranChapters = quranChapters
                                        .sortedBy { a -> chaptersOrder.indexOfFirst { it == a.id } }
                                }
                            }
                        }
                    }
            }
            launch {
                getNextQuranReadingSectionsFlow.call().collectLatest { sections ->
                    withContext(Dispatchers.Main) {
                        state.sections = sections
                    }
                }
            }
            launch {
                loadQuranReadingSuggestions.call()
            }
        }

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
            is QuranEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            QuranEvent.OnLoadQuranSectionsClicked -> onLoadQuranSectionsClicked()
            QuranEvent.OnNextSectionClicked -> onNextSectionClicked()
            QuranEvent.OnViewFullClicked -> onViewFullClicked()
            QuranEvent.OnStart -> onStart()
        }
    }

    private fun onStart() {
        if (hasChaptersBeenSorted) {
            state.quranChapters = state.quranChapters.sortedBy { it.id }
                .sortedByDescending { it.isMemorized }
                .also {
                    chaptersOrder = it.map { chapter ->
                        chapter.id
                    }
                }
        }
        sendUiEvent(UiEvent.ScrollListToPosition(0))
    }

    private fun addMemorizedChapterAyat(chapterId: Int, fromVerse: Int, toVerse: Int) {
        tracker.trackQuranChapterAdd()
        viewModelScope.launch(Dispatchers.IO) {
            addMemorizedChapterAyat.call(chapterId, fromVerse, toVerse)
                .onFailure {
                    sendUiEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.toString())))
                }
        }
    }

    private fun editMemorizedChapterAyat(chapterId: Int, fromVerse: Int, toVerse: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            editMemorizedChapterAyat.call(chapterId, fromVerse, toVerse)
                .onFailure {
                    sendUiEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.toString())))
                }
        }
    }

    private fun removeMemorizedChapterAyat(chapterId: Int) {
        tracker.trackQuranChapterRemove()
        viewModelScope.launch(Dispatchers.IO) {
            removeMemorizedChapterAyat.call(chapterId)
                .onFailure {
                    sendUiEvent(UiEvent.ShowErrorSnackBar(UiText.DynamicString(it.toString())))
                }
        }
    }

    private fun onLoadQuranSectionsClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            loadQuranReadingSuggestions.call()
        }
    }

    private fun onNextSectionClicked() {
        tracker.trackButtonClicked(TrackedButtons.NEXT_QURAN_READING_SECTION)
        state.sections?.let { sections ->
            viewModelScope.launch(Dispatchers.IO) {
                markQuranSectionAsRead.call(sections)
            }
        }
    }

    private fun onViewFullClicked() {
        tracker.trackButtonClicked(TrackedButtons.VIEW_FULL_QURAN_READING_SECTION)
        val sections = state.sections ?: return
        sendUiEvent(
            UiEvent.Navigate(
                Route.FullQuranSections,
                listOf(toJson(sections))
            )
        )
    }

    private fun onSearchQueryChanged(query: String) {
        state.searchQuery = query.trim()
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(uiEvent)
        }
    }
}