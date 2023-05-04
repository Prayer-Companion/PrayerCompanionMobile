package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus

@Preview
@Composable
fun PrayerStatusDropDownMenu(
    expanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onItemSelected: (PrayerStatus) -> Unit = {}
) {
    //TODO either don't allow updating future prayers, or add a "Not set" value
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        PrayerStatusMenuItem(
            modifier = Modifier.clickable {
                onItemSelected(PrayerStatus.Jamaah)
            },
            prayerStatus = PrayerStatus.Jamaah
        )
        PrayerStatusMenuItem(
            modifier = Modifier.clickable {
                onItemSelected(PrayerStatus.OnTime)
            },
            prayerStatus = PrayerStatus.OnTime
        )
        PrayerStatusMenuItem(
            modifier = Modifier.clickable {
                onItemSelected(PrayerStatus.Late)
            },
            prayerStatus = PrayerStatus.Late
        )
        PrayerStatusMenuItem(
            modifier = Modifier.clickable {
                onItemSelected(PrayerStatus.Missed)
            },
            prayerStatus = PrayerStatus.Missed
        )
        PrayerStatusMenuItem(
            modifier = Modifier.clickable {
                onItemSelected(PrayerStatus.Qadaa)
            },
            prayerStatus = PrayerStatus.Qadaa
        )
    }
}
