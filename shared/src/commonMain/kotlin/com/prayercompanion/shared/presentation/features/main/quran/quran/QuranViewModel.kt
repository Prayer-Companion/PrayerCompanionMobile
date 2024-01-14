package com.prayercompanion.shared.presentation.features.main.quran.quran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.prayercompanion.shared.domain.usecases.quran.AddMemorizedChapterAyat
import com.prayercompanion.shared.domain.usecases.quran.EditMemorizedChapterAyat
import com.prayercompanion.shared.domain.usecases.quran.GetFullQuranWithMemorized
import com.prayercompanion.shared.domain.usecases.quran.GetNextQuranReadingSections
import com.prayercompanion.shared.domain.usecases.quran.LoadQuranReadingSuggestions
import com.prayercompanion.shared.domain.usecases.quran.MarkQuranSectionAsRead
import com.prayercompanion.shared.domain.usecases.quran.RemoveMemorizedChapterAyat
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
) : ScreenModel {

    private var hasChaptersBeenSorted = false
    private var chaptersOrder = listOf<Int>()
    var state: QuranState by mutableStateOf(QuranState())
        private set
    private val _uiEventsChannel = Channel<UiEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    init {
        screenModelScope.launch(Dispatchers.IO) {
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
        screenModelScope.launch(Dispatchers.IO) {
            addMemorizedChapterAyat.call(chapterId, fromVerse, toVerse)
                .onFailure {
                    sendUiEvent(UiEvent.ShowMessage(UiText.DynamicString(it.toString())))
                }
        }
    }

    private fun editMemorizedChapterAyat(chapterId: Int, fromVerse: Int, toVerse: Int) {
        screenModelScope.launch(Dispatchers.IO) {
            editMemorizedChapterAyat.call(chapterId, fromVerse, toVerse)
                .onFailure {
                    sendUiEvent(UiEvent.ShowMessage(UiText.DynamicString(it.toString())))
                }
        }
    }

    private fun removeMemorizedChapterAyat(chapterId: Int) {
        tracker.trackQuranChapterRemove()
        screenModelScope.launch(Dispatchers.IO) {
            removeMemorizedChapterAyat.call(chapterId)
                .onFailure {
                    sendUiEvent(UiEvent.ShowMessage(UiText.DynamicString(it.toString())))
                }
        }
    }

    private fun onLoadQuranSectionsClicked() {
        screenModelScope.launch(Dispatchers.IO) {
            loadQuranReadingSuggestions.call()
        }
    }

    private fun onNextSectionClicked() {
        tracker.trackButtonClicked(TrackedButtons.NEXT_QURAN_READING_SECTION)
        state.sections?.let { sections ->
            screenModelScope.launch(Dispatchers.IO) {
                markQuranSectionAsRead.call(sections)
            }
        }
    }

    private fun onViewFullClicked() {
        tracker.trackButtonClicked(TrackedButtons.VIEW_FULL_QURAN_READING_SECTION)
        sendUiEvent(
            UiEvent.Navigate(Route.FullQuranSections)
        )
    }

    private fun onSearchQueryChanged(query: String) {
        state.searchQuery = query.trim()
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        screenModelScope.launch {
            _uiEventsChannel.send(uiEvent)
        }
    }
}