package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts

@Preview(locale = "ar")
@Composable
fun PrayerItem(
    modifier: Modifier = Modifier,
    name: String = "العصر",
    prayerInfo: PrayerInfo = PrayerInfo.Default.copy(status = PrayerStatus.Jamaah),
    onStatusSelected: (PrayerStatus, PrayerInfo) -> Unit = { _, _ -> }
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    var isStatusSelectorExpanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .height(55.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(
                        topStart = 15.dp,
                        bottomStart = 15.dp
                    )
                )
                .weight(1f)
                .padding(start = spacing.spaceMedium),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                text = name,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = prayerInfo.time.format(PresentationConsts.TimeFormatter),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary
            )
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .defaultMinSize(minWidth = 92.dp)
                .background(
                    color = prayerInfo.status.color,
                    shape = RoundedCornerShape(
                        topEnd = 15.dp,
                        bottomEnd = 15.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (prayerInfo.status != PrayerStatus.NotSet) {
                Text(
                    text = stringResource(id = prayerInfo.status.nameId),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button
                )
            }

            Icon(
                modifier = Modifier.clickable {
                    isStatusSelectorExpanded = !isStatusSelectorExpanded
                },
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = MaterialTheme.colors.onPrimary
            )
            PrayerStatusDropDownMenu(
                expanded = isStatusSelectorExpanded,
                onItemSelected = {
                    onStatusSelected(it, prayerInfo)
                    isStatusSelectorExpanded = false
                },
                onDismissRequest = {
                    isStatusSelectorExpanded = !isStatusSelectorExpanded
                }
            )
        }
    }
}