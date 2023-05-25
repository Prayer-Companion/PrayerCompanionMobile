package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus

@Preview
@Composable
fun PrayerStatusMenuItem(
    modifier: Modifier = Modifier,
    prayerStatus: PrayerStatus = PrayerStatus.Jamaah,
    showDivider: Boolean = false,
    backgroundShape: Shape = RectangleShape
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.onPrimary,
                shape = backgroundShape
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp),
            text = stringResource(id = prayerStatus.nameId),
            color = prayerStatus.color,
            style = MaterialTheme.typography.h3
        )
        if (showDivider)
            Divider(modifier = Modifier.height(0.5.dp), color = Color.Black.copy(alpha = 0.5f))
    }
}