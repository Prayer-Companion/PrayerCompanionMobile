package com.prayercompanion.prayercompanionandroid.presentation.features.quran.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.prayercompanionandroid.domain.models.quran.QuranVerse
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Composable
fun QuranSection(
    modifier: Modifier = Modifier,
    title: String,
    section: PrayerQuranReadingSection,
    numberOfLines: Int = Int.MAX_VALUE
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bullet_point),
                contentDescription = "",
                tint = MaterialTheme.colors.primaryVariant
            )
            Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )
        }
        Spacer(modifier = Modifier.height(spacing.spaceSmall))

        //the chapter verses should always show in a right-to-left view regardless of the device language
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column {
                Text(
                    text = stringResource(
                        id = R.string.quran_reading_section_chapter_name,
                        section.chapterName,
                        section.startVerse,
                        section.endVerse
                    ),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    text = stringResource(
                        id = R.string.quran_reading_section,
                        section.verses.joinToString { "${it.text} (${it.index})" }
                    ),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary,
                    lineHeight = 30.sp,
                    maxLines = numberOfLines
                )
            }
        }
    }
}

@Preview(locale = "en")
@Composable
private fun QuranSectionPreview() = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(spacing.spaceMedium)
    ) {
        QuranSection(
            title = stringResource(id = R.string.first_quran_reading_section),
            numberOfLines = 1,
            section = PrayerQuranReadingSection(
                sectionId = 0,
                chapterId = 0,
                chapterName = "المدثر",
                startVerse = 0,
                endVerse = 5,
                verses = listOf(
                    QuranVerse(
                        index = 1,
                        text = "يَا أَيُّهَا الْمُدَّثِّرُ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 2,
                        text = "قُمْ فَأَنذِرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 3,
                        text = "وَرَبَّكَ فَكَبِّرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 4,
                        text = "وَثِيَابَكَ فَطَهِّرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 5,
                        text = "وَالرُّجْزَ فَاهْجُرْ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 6,
                        text = "وَلَا تَمْنُن تَسْتَكْثِرُ",
                        hasBismillah = false
                    ),
                    QuranVerse(
                        index = 7,
                        text = "وَلِرَبِّكَ فَاصْبِرْ",
                        hasBismillah = false
                    ),

                    )
            )
        )
    }
}