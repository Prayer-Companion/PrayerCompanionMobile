package com.prayercompanion.prayercompanionandroid.presentation.features.quran.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranChapter
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.theme.SmallTextStyle

@Composable
fun QuranChapterItem(
    modifier: Modifier = Modifier,
    quranChapter: QuranChapter,
    onCheckedChange: (isChecked: Boolean, from: Int, to: Int) -> Unit,
    onSaveClicked: (from: Int, to: Int) -> Unit
) = PrayerCompanionAndroidTheme {

    val spacing = LocalSpacing.current
    val isInPreviewMode = LocalInspectionMode.current

    var isSurahChecked by remember {
        mutableStateOf(quranChapter.isMemorized)
    }
    var isEditMode by remember {
        //as an initial value, open the edit mode in preview time, and close it on run
        //this is just an initial value, and afterwards it is adjusted by clicking on the edit icon
        mutableStateOf(isInPreviewMode)
    }
    var fromVerseText by remember {
        mutableStateOf(quranChapter.memorizedFrom.toString())
    }
    var toVerseText by remember {
        mutableStateOf(quranChapter.memorizedTo.toString())
    }

    fun getFromVerse() = fromVerseText.toIntOrNull() ?: 1
    fun getToVerse() = toVerseText.toIntOrNull() ?: quranChapter.versesCount

    fun applyIfValidInput(from: Int, to: Int, block: () -> Unit) {
        if (
            from in 1..quranChapter.versesCount &&
            to in 1..quranChapter.versesCount &&
            from <= to
        ) {
            block.invoke()
        }
    }

    @Composable
    fun inputField(
        title: String,
        text: String,
        onTextChange: (String) -> Unit,
        onMinusClicked: () -> Unit,
        onAddClicked: () -> Unit,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onPrimary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        onMinusClicked()
                    },
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Minus",
                    tint = MaterialTheme.colors.onPrimary
                )
                TextField(
                    modifier = Modifier.width(60.dp),
                    value = text,
                    onValueChange = onTextChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                        disabledIndicatorColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = Color.Transparent,
                        textColor = MaterialTheme.colors.onPrimary,
                        cursorColor = MaterialTheme.colors.onPrimary
                    ),
                    textStyle = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center)
                )
                Icon(
                    modifier = Modifier.clickable {
                        onAddClicked()
                    },
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }

    Column(
        modifier
            .shadow(
                //only show shadow when the edit layout is open
                elevation = if (isEditMode) 1.dp else 0.dp,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .padding(horizontal = spacing.spaceMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSurahChecked,
                    onCheckedChange = {
                        isSurahChecked = it
                        onCheckedChange(it, getFromVerse(), getToVerse())
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colors.onPrimary,
                        checkedColor = MaterialTheme.colors.onPrimary,
                        checkmarkColor = MaterialTheme.colors.primary
                    )
                )
                Text(
                    text = quranChapter.id.toString(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                Column {
                    Text(
                        text = quranChapter.name,
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        text = stringResource(id = R.string.verses_count, quranChapter.versesCount),
                        style = SmallTextStyle,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier.padding(bottom = spacing.spaceExtraSmall),
                    text = stringResource(id = R.string.from_verse),
                    style = SmallTextStyle,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                Text(
                    text = quranChapter.memorizedFrom.toString(),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                Text(
                    modifier = Modifier.padding(bottom = spacing.spaceExtraSmall),
                    text = stringResource(id = R.string.to_verse),
                    style = SmallTextStyle,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                Text(
                    text = quranChapter.memorizedTo.toString(),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                if (isSurahChecked) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                isEditMode = !isEditMode
                            },
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
        if (isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(90.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(bottomStart = 15.dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    inputField(
                        title = stringResource(id = R.string.edit_aya_from),
                        text = fromVerseText,
                        onTextChange = {
                            applyIfValidInput(
                                it.toIntOrNull() ?: 1,
                                getToVerse()
                            ) {
                                fromVerseText = it
                            }
                        },
                        onMinusClicked = {
                            val from = getFromVerse() - 1
                            applyIfValidInput(
                                from = from,
                                to = getToVerse()
                            ) {
                                fromVerseText = from.toString()
                            }
                        },
                        onAddClicked = {
                            val from = getFromVerse() + 1
                            applyIfValidInput(
                                from = from,
                                to = getToVerse()
                            ) {
                                fromVerseText = from.toString()
                            }
                        }
                    )
                    inputField(
                        title = stringResource(id = R.string.edit_aya_to),
                        text = toVerseText,
                        onTextChange = {
                            applyIfValidInput(
                                from = getFromVerse(),
                                to = it.toIntOrNull() ?: quranChapter.versesCount
                            ) {
                                toVerseText = it
                            }
                        },
                        onMinusClicked = {
                            val to = getToVerse() - 1
                            applyIfValidInput(
                                from = getFromVerse(),
                                to = to
                            ) {
                                toVerseText = to.toString()
                            }
                        },
                        onAddClicked = {
                            val to = getToVerse() + 1
                            applyIfValidInput(
                                from = getFromVerse(),
                                to = to
                            ) {
                                toVerseText = to.toString()
                            }
                        }
                    )
                }
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                        .background(
                            color = MaterialTheme.colors.primaryVariant,
                            shape = RoundedCornerShape(bottomEnd = 15.dp)
                        ),
                    onClick = {
                        onSaveClicked(
                            getFromVerse(),
                            getToVerse()
                        )
                        isEditMode = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

@Preview(locale = "ar")
@Composable
private fun QuranItemPreview() {
    QuranChapterItem(
        modifier = Modifier.fillMaxWidth(),
        quranChapter = QuranChapter(
            index = 1,
            name = "المدثر",
            verses = listOf()
        ),
        onCheckedChange = { _, _, _ ->

        },
        onSaveClicked = { _, _ ->

        }
    )
}