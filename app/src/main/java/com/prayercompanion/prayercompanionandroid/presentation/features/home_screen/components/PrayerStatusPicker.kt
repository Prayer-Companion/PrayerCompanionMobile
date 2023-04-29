package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus

@Preview
@Composable
fun PrayerStatusPicker(
    modifier: Modifier = Modifier,
    onStatusSelected: (PrayerStatus) -> Unit = {}
) {
    var isStatusSelectorExpanded by remember {
        mutableStateOf(false)
    }

    Column {
        Button(
            modifier = modifier,
            onClick = {
                isStatusSelectorExpanded = !isStatusSelectorExpanded
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.status))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop Down")
            }
        }
        PrayerStatusDropDownMenu(
            expanded = isStatusSelectorExpanded,
            onItemSelected = {
                onStatusSelected(it)
                isStatusSelectorExpanded = false
            },
            onDismissRequest = {
                isStatusSelectorExpanded = !isStatusSelectorExpanded
            }
        )
    }
}