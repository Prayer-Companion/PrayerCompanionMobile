package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.presentation.theme.DarkGrey
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts

@Preview(locale = "ar")
@Composable
fun PrayerItem(
    modifier: Modifier = Modifier,
    name: String = "العصر",
    prayerInfo: PrayerInfo = PrayerInfo.Default.copy(status = PrayerStatus.Jamaah),
    onStatusSelected: (PrayerStatus, PrayerInfo) -> Unit = { _,_->}
) {
    val spacing = LocalSpacing.current
    var isStatusSelectorExpanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .height(65.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    Color.White,
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
                style = MaterialTheme.typography.body1,
                color = DarkGrey
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = prayerInfo.time.format(PresentationConsts.TimeFormatter),
                style = MaterialTheme.typography.body1,
                color = DarkGrey
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .background(
                    color = prayerInfo.status.color,
                    shape = RoundedCornerShape(
                        topEnd = 15.dp,
                        bottomEnd = 15.dp
                    )
                )
                .clickable {
                    isStatusSelectorExpanded = !isStatusSelectorExpanded
                },
            contentAlignment = Alignment.Center,
        ) {
            if (prayerInfo.status != PrayerStatus.NotSet) {
                Icon(
                    painter = painterResource(id = prayerInfo.status.iconId),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
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