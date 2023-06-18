package com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.components.TitleHeader
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.components.QuranChapterItem
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.components.QuranSection
import com.prayercompanion.prayercompanionandroid.presentation.theme.AppBackground
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
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

    LaunchedEffect(key1 = true) {
        uiEventsChannel.collect {
            when (it) {
                is UiEvent.ShowErrorSnackBar -> {
                    showSnackBar(it.errorMessage.asString(context))
                }

                is UiEvent.Navigate -> {
                    navigate(it) {}
                }

                else -> Unit
            }
        }
    }

    Box {
        AppBackground()
        Column(modifier = Modifier.fillMaxSize()) {
            TitleHeader(title = stringResource(id = R.string.quran_title))
            val readingSections = state.sections
            val quranSectionsHeightFraction = if (readingSections != null) 0.4f else 0.2f
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(quranSectionsHeightFraction)
                    .padding(top = spacing.spaceMedium, bottom = spacing.spaceSmall)
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
                        .weight(1f)
                        .padding(spacing.spaceMedium)
                ) {
                    if (readingSections != null) {
                        QuranSection(
                            modifier = Modifier.weight(1f),
                            stringResource(id = R.string.first_quran_reading_section),
                            readingSections.firstSection
                        )
                        Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        QuranSection(
                            modifier = Modifier.weight(1f),
                            stringResource(id = R.string.second_quran_reading_section),
                            readingSections.secondSection
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.quranChapters.any { it.isMemorized }) {
                                Text(
                                    modifier = Modifier.clickable {
                                        onEvent(QuranEvent.OnLoadQuranSectionsClicked)
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
                                shape = RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp)
                            )
                            .padding(
                                horizontal = spacing.spaceExtraSmall
                            ),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onEvent(QuranEvent.OnViewFullClicked) },
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
                            onClick = { onEvent(QuranEvent.OnNextSectionClicked) },
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

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium),
                value = state.searchQuery,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colors.onPrimary,
                    )
                },
                onValueChange = {
                    onEvent(QuranEvent.OnSearchQueryChanged(it))
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
                    .padding(vertical = spacing.spaceSmall, horizontal = 2.dp)
            ) {
                items(
                    items = state.filteredQuranChapters,
                    key = { it.id }
                ) { chapter ->
                    QuranChapterItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        quranChapter = chapter,
                        onCheckedChange = { selected, from, to ->
                            if (selected) {
                                onEvent(QuranEvent.OnChapterSelected(chapter.id, from, to))
                            } else {
                                onEvent(QuranEvent.OnChapterDeselected(chapter.id))
                            }
                        },
                        onSaveClicked = { from, to ->
                            onEvent(QuranEvent.OnChapterAyatSaved(chapter.id, from, to))
                        }
                    )
                }
            }
        }
    }
}
