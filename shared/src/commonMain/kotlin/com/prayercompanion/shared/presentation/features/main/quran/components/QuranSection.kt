package com.prayercompanion.shared.presentation.features.main.quran.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.utils.stringResource
import dev.icerock.moko.resources.compose.painterResource

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
                painter = painterResource(Res.images.ic_bullet_point),
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
                        id = Res.strings.quran_reading_section_chapter_name,
                        args = listOf(
                            section.chapterName,
                            section.startVerse,
                            section.endVerse
                        )
                    ),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    text = stringResource(
                        id = Res.strings.quran_reading_section,
                        args = listOf(section.verses.joinToString { "${it.text} (${it.index})" })
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