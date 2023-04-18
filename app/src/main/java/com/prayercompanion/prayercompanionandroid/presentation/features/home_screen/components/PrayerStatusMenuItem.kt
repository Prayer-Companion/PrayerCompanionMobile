package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus

@Preview
@Composable
fun PrayerStatusMenuItem(
    modifier: Modifier = Modifier,
    prayerStatus: PrayerStatus = PrayerStatus.Jamaah
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .defaultMinSize(80.dp, 40.dp)
                .background(color = MaterialTheme.colors.onPrimary)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = prayerStatus.iconId),
                contentDescription = stringResource(id = prayerStatus.nameId),
                tint = prayerStatus.color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = prayerStatus.nameId),
                color = prayerStatus.color,
                style = MaterialTheme.typography.body1
            )
        }
    }
}