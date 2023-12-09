package com.prayercompanion.shared.presentation.features.main.quran.quran

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
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.shared.domain.models.quran.QuranChapter
import com.prayercompanion.shared.presentation.components.AppBackground
import com.prayercompanion.shared.presentation.components.TitleHeader
import com.prayercompanion.shared.presentation.features.main.quran.components.QuranChapterItem
import com.prayercompanion.shared.presentation.features.main.quran.components.QuranSection
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.StringResourceReader
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.asString
import com.prayercompanion.shared.presentation.utils.compose.LifecycleEvent
import com.prayercompanion.shared.presentation.utils.compose.OnLifecycleEvent
import com.prayercompanion.shared.presentation.utils.stringResource
import com.prayercompanion.shared.presentation.utils.toScreen
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class QuranScreen(private val scaffoldState: ScaffoldState) : Screen, KoinComponent {

    private val stringResourceReader: StringResourceReader by inject()

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<QuranViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        QuranScreen(
            navigate = {
                navigator.push(it.route.toScreen())
            },
            state = viewModel.state,
            onEvent = viewModel::onEvent,
            uiEventsChannel = viewModel.uiEventsChannel,
            stringResourceReader = stringResourceReader,
            scaffoldState = scaffoldState
        )
    }
}

@Composable
fun QuranScreen(
    navigate: (UiEvent.Navigate) -> Unit,
    state: QuranState,
    onEvent: (QuranEvent) -> Unit,
    uiEventsChannel: Flow<UiEvent>,
    stringResourceReader: StringResourceReader,
    scaffoldState: ScaffoldState
) = PrayerCompanionAndroidTheme {

    val spacing = LocalSpacing.current
    val chaptersListState = rememberLazyListState()
//    val keyboardConfig by keyboardAsState() todo kmp

    OnLifecycleEvent { event ->
        if (event == LifecycleEvent.ON_START) {
            onEvent(QuranEvent.OnStart)
        }
    }

    LaunchedEffect(key1 = true) {
        uiEventsChannel.collect {
            when (it) {
                is UiEvent.ShowErrorSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        it.errorMessage.asString(
                            stringResourceReader
                        )
                    )
                }


                is UiEvent.Navigate -> navigate(it)
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
            TitleHeader(title = stringResource(id = Res.strings.quran_title))
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
//                if (keyboardConfig == KeyboardConfig.Closed) {
                QuranReadingSections(
                    readingSections = state.sections,
                    hasAnyMemorizedChapters = state.hasAnyMemorizedChapters,
                    onLoadQuranSectionsClicked = { onEvent(QuranEvent.OnLoadQuranSectionsClicked) },
                    onViewFullClicked = { onEvent(QuranEvent.OnViewFullClicked) },
                    onNextSectionClicked = { onEvent(QuranEvent.OnNextSectionClicked) }
                )
//                }
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
                    title = stringResource(id = Res.strings.first_quran_reading_section),
                    section = readingSections.firstSection,
                    numberOfLines = 1
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                QuranSection(
                    title = stringResource(id = Res.strings.second_quran_reading_section),
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
                            text = stringResource(id = Res.strings.load_quran_sections),
                            style = MaterialTheme.typography.body2,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        Text(
                            text = stringResource(id = Res.strings.empty_quran_sections_prompt),
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
                        text = stringResource(id = Res.strings.show_full_quran_reading_sections),
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
                        text = stringResource(id = Res.strings.next_quran_reading_sections),
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
                    text = stringResource(id = Res.strings.chapter_name),
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