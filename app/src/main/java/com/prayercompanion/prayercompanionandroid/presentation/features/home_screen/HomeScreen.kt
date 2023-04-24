package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components.HomeHeader
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components.PrayerItem
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEventsChannel.collect {
            when (it) {
                is UiEvent.ShowErrorSnackBar -> {
                    scaffoldState
                        .snackbarHostState
                        .showSnackbar(it.errorMessage.asString(context))
                }
            }
        }
    }
    Column(
        modifier = Modifier.background(MaterialTheme.colors.primaryVariant),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HomeHeader(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            prayerInfo = viewModel.nextPrayer,
            durationUntilNextPrayer = viewModel.durationUntilNextPrayer,
            onStatusSelected = viewModel::onStatusSelected
        )
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.onNextDayButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "next day"
                )
            }
            Text(text = viewModel.currentDate.format(PresentationConsts.DateFormatter))
            IconButton(onClick = {
                viewModel.onPreviousDayButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "previous day"
                )
            }
        }
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(spacing.spaceMedium, 0.dp)
        ) {
            items(viewModel.dayPrayersInfo.prayers) {
                PrayerItem(
                    name = stringResource(id = it.prayer.nameId),
                    modifier = Modifier.fillMaxWidth(),
                    prayerInfo = it
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
            }
        }
    }
}