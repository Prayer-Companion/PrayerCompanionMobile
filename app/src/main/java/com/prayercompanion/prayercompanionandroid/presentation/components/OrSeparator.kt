package com.prayercompanion.prayercompanionandroid.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing

@Preview
@Composable
fun OrSeparator(
    modifier: Modifier = Modifier,
    name: String = stringResource(id = R.string.or)
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .background(Color.Black)
                .height(1.dp)
                .weight(1f)
        )
        Spacer(
            modifier = Modifier
                .width(spacing.spaceMedium)
        )
        Text(text = name)
        Spacer(
            modifier = Modifier
                .width(spacing.spaceMedium)
        )
        Spacer(
            modifier = Modifier
                .background(Color.Black)
                .height(1.dp)
                .weight(1f)
        )
    }
}