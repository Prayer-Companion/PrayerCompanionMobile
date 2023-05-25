package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Preview
@Composable
fun PrayerStatusPicker(
    modifier: Modifier = Modifier,
    onStatusSelected: (PrayerStatus) -> Unit = {}
) = PrayerCompanionAndroidTheme {
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
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            contentPadding = PaddingValues(
                top = 3.dp,
                bottom = 3.dp,
                start = 24.dp,
                end = 16.dp
            )
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.status),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    modifier = Modifier.rotate(if (isStatusSelectorExpanded) 180f else 0f),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop Down",
                    tint = MaterialTheme.colors.onPrimary
                )
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