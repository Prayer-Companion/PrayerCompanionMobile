package com.prayercompanion.prayercompanionandroid.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Preview
@Composable
fun TitleHeader(
    modifier: Modifier = Modifier,
    title: String = "Memorized Ayat"
) = PrayerCompanionAndroidTheme {

    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)
            )
            .padding(horizontal = spacing.spaceLarge, vertical = spacing.spaceMedium)
    ) {
        Text(
            modifier = Modifier.align(Alignment.BottomStart),
            text = title,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onPrimary,
        )
    }
}