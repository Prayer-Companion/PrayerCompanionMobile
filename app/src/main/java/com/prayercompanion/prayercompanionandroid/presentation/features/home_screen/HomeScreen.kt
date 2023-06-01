package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components.HomeHeader
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components.PrayerItem
import com.prayercompanion.prayercompanionandroid.presentation.theme.AppBackground
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.utils.PresentationConsts
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            viewModel.onLocationSettingsResult(result.resultCode == Activity.RESULT_OK)
        }
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect {
            when (it) {
                is UiEvent.ShowErrorSnackBar -> {
                    scaffoldState
                        .snackbarHostState
                        .showSnackbar(it.errorMessage.asString(context))
                }
                is UiEvent.LaunchIntentSenderRequest -> {
                    locationSettingsLauncher.launch(it.intentSenderRequest)
                }
                else -> Unit
            }
        }
    }

    Box {
        AppBackground(
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HomeHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                currentPrayer = viewModel.state.currentPrayer,
                nextPrayer = viewModel.state.nextPrayer,
                durationUntilNextPrayer = viewModel.durationUntilNextPrayer,
                onStatusSelected = viewModel::onStatusSelected,
                statusesCounts = viewModel.state.lastWeekStatuses
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            // TODO: add a way to get back to today's date quickly
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .background(
                                color = MaterialTheme.colors.primary,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            viewModel.onPreviousDayButtonClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "previous day",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                        Text(
                            text = viewModel.state.selectedDate.format(PresentationConsts.DateFormatter),
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.onPrimary
                        )
                        IconButton(onClick = {
                            viewModel.onNextDayButtonClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "next day",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(spacing.spaceMedium, 0.dp)
            ) {
                items(viewModel.state.selectedDayPrayersInfo.prayers) {
                    PrayerItem(
                        name = stringResource(id = it.prayer.nameId),
                        modifier = Modifier.fillMaxWidth(),
                        prayerInfo = it,
                        onStatusSelected = viewModel::onStatusSelected
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceSmall))
                }
            }
        }
    }
}