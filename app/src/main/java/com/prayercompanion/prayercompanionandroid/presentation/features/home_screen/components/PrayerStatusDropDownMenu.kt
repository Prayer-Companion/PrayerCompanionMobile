package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus

@Preview
@Composable
fun PrayerStatusDropDownMenu(
    expanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onItemSelected: (PrayerStatus) -> Unit = {}
) {
    //TODO either don't allow updating future prayers, or add a "Not set" value
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        val statuses = PrayerStatus.values()
        statuses.forEachIndexed { index, status ->
            PrayerStatusMenuItem(
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp, minHeight = 32.dp)
                    .clickable {
                        onItemSelected(status)
                    },
                prayerStatus = status,
                showDivider = index != statuses.size - 1,
                backgroundShape = when (index) {
                    0 -> {
                        RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                    }
                    (statuses.size - 1) -> {
                        RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp)
                    }
                    else -> RectangleShape
                }
            )
        }
    }
}
