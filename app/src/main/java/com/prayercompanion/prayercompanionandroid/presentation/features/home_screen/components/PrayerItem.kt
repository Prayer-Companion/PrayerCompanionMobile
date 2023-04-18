package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import com.prayercompanion.prayercompanionandroid.presentation.theme.DarkGrey
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts

@Composable
fun PrayerItem(
    modifier: Modifier = Modifier,
    name: String,
    prayerInfo: PrayerInfo
) {
    val spacing = LocalSpacing.current

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
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (prayerInfo.status != PrayerStatus.NotSet) {
                Icon(
                    painter = painterResource(id = prayerInfo.status.iconId),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}