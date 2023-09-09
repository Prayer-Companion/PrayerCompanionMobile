package com.prayercompanion.prayercompanionandroid.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.autoMirror
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Composable
fun TitleHeader(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null
) = PrayerCompanionAndroidTheme {

    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)
            )
            .padding(horizontal = spacing.spaceLarge, vertical = spacing.spaceMedium),
    ) {
        if (onBack != null) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    modifier = Modifier.autoMirror(),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onPrimary,
        )
    }
}

@Preview
@Composable
fun TitleHeaderPreview() {
    TitleHeader(title = "Memorized Ayat")
}