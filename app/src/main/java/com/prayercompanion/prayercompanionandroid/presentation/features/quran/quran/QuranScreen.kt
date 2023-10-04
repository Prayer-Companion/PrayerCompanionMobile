package com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.components.AppBackground
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.components.QuranChapterItem
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.components.QuranSection
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.compose.KeyboardConfig
import com.prayercompanion.prayercompanionandroid.presentation.utils.compose.OnLifecycleEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.compose.keyboardAsState
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.domain.models.quran.QuranChapter
import com.prayercompanion.shared.presentation.components.TitleHeader
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Preview(locale = "ar", showSystemUi = true)
@Composable
fun QuranScreen(
    navigate: (UiEvent.Navigate, NavOptionsBuilder.() -> Unit) -> Unit = { _, _ -> },
    state: QuranState = QuranState(),
    onEvent: (QuranEvent) -> Unit = {},
    uiEventsChannel: Flow<UiEvent> = emptyFlow(),
    showSnackBar: suspend (String) -> Unit = {},
) = PrayerCompanionAndroidTheme {

    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val chaptersListState = rememberLazyListState()
    val keyboardConfig by keyboardAsState()

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_START) {
            onEvent(QuranEvent.OnStart)
        }
    }

    LaunchedEffect(key1 = true) {
        uiEventsChannel.collect {
            when (it) {
                is UiEvent.ShowErrorSnackBar -> showSnackBar(it.errorMessage.asString(context))
                is UiEvent.Navigate -> navigate(it) {}
                is UiEvent.ScrollListToPosition -> chaptersListState.scrollToItem(it.position)
                else -> Unit
            }
        }
    }

    Box {
        AppBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TitleHeader(title = stringResource(id = R.string.quran_title))
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                if (keyboardConfig == KeyboardConfig.Closed) {
                    QuranReadingSections(
                        readingSections = state.sections,
                        hasAnyMemorizedChapters = state.hasAnyMemorizedChapters,
                        onLoadQuranSectionsClicked = { onEvent(QuranEvent.OnLoadQuranSectionsClicked) },
                        onViewFullClicked = { onEvent(QuranEvent.OnViewFullClicked) },
                        onNextSectionClicked = { onEvent(QuranEvent.OnNextSectionClicked) }
                    )
                }
            }

            QuranChaptersSearchAndList(
                searchQuery = state.searchQuery,
                listState = chaptersListState,
                quranChapter = state.filteredQuranChapters,
                onSearchQueryChanged = {
                    onEvent(QuranEvent.OnSearchQueryChanged(it))
                },
                onChapterSelected = { id, from, to ->
                    onEvent(QuranEvent.OnChapterSelected(id, from, to))
                },
                onChapterDeselected = { id ->
                    onEvent(QuranEvent.OnChapterDeselected(id))
                },
                onChapterAyatSaved = { id, from, to ->
                    onEvent(QuranEvent.OnChapterAyatSaved(id, from, to))
                }
            )
        }
    }
}


@Composable
private fun QuranReadingSections(
    readingSections: PrayerQuranReadingSections?,
    hasAnyMemorizedChapters: Boolean,
    onLoadQuranSectionsClicked: () -> Unit,
    onViewFullClicked: () -> Unit,
    onNextSectionClicked: () -> Unit,
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacing.spaceMedium)
            .padding(horizontal = spacing.spaceMedium)
    ) {
        val shape = if (readingSections != null) {
            RoundedCornerShape(
                topEnd = 15.dp,
                topStart = 15.dp
            )
        } else {
            RoundedCornerShape(15.dp)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = shape
                )
                .padding(spacing.spaceMedium)
        ) {
            if (readingSections != null) {
                QuranSection(
                    title = stringResource(id = R.string.first_quran_reading_section),
                    section = readingSections.firstSection,
                    numberOfLines = 1
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                QuranSection(
                    title = stringResource(id = R.string.second_quran_reading_section),
                    section = readingSections.secondSection,
                    numberOfLines = 1
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.spaceMedium),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasAnyMemorizedChapters) {
                        Text(
                            modifier = Modifier.clickable {
                                onLoadQuranSectionsClicked()
                            },
                            text = stringResource(id = R.string.load_quran_sections),
                            style = MaterialTheme.typography.body2,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.empty_quran_sections_prompt),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
        if (readingSections != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primaryVariant,
                        shape = RoundedCornerShape(
                            bottomEnd = 15.dp,
                            bottomStart = 15.dp
                        )
                    )
                    .padding(
                        horizontal = spacing.spaceExtraSmall
                    ),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onViewFullClicked() },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.show_full_quran_reading_sections),
                        style = MaterialTheme.typography.body2,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                TextButton(
                    onClick = { onNextSectionClicked() },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.next_quran_reading_sections),
                        style = MaterialTheme.typography.body2,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun QuranChaptersSearchAndList(
    searchQuery: String,
    listState: LazyListState,
    quranChapter: List<QuranChapter>,
    onSearchQueryChanged: (query: String) -> Unit,
    onChapterSelected: (id: Int, from: Int, to: Int) -> Unit,
    onChapterDeselected: (id: Int) -> Unit,
    onChapterAyatSaved: (id: Int, from: Int, to: Int) -> Unit
) {
    Column {
        val spacing = LocalSpacing.current
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.spaceMedium),
            value = searchQuery,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colors.onPrimary,
                )
            },
            onValueChange = {
                onSearchQueryChanged(it)
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.chapter_name),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle2
                )
            },
            colors = TextFieldDefaults
                .textFieldColors(
                    textColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.onPrimary,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            textStyle = MaterialTheme.typography.subtitle2,
            singleLine = true,
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
        )
        Box(
            modifier = Modifier
                .height(2.dp)
                .background(color = MaterialTheme.colors.secondary),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing.spaceMedium)
                .padding(bottom = spacing.spaceMedium)
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp)
                )
                .padding(vertical = spacing.spaceSmall, horizontal = 2.dp),
            state = listState
        ) {
            items(
                items = quranChapter,
                key = { it.id }
            ) { chapter ->
                QuranChapterItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    quranChapter = chapter,
                    onCheckedChange = { selected, from, to ->
                        if (selected) {
                            onChapterSelected(chapter.id, from, to)
                        } else {
                            onChapterDeselected(chapter.id)
                        }
                    },
                    onSaveClicked = { from, to ->
                        onChapterAyatSaved(chapter.id, from, to)
                    }
                )
            }
        }
    }
}