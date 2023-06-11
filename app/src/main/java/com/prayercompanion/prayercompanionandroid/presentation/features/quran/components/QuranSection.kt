package com.prayercompanion.prayercompanionandroid.presentation.features.quran.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSection
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing

@Composable
fun QuranSection(
    modifier: Modifier,
    title: String,
    section: PrayerQuranReadingSection,
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
        Text(
            text = stringResource(
                id = R.string.quran_reading_section,
                section.chapterName,
                section.startVerse,
                section.endVerse,
                section.verses.joinToString { "${it.text} (${it.index})" }
            ),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onPrimary,
            lineHeight = 25.sp
        )
    }
}