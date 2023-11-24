package com.prayercompanion.shared.presentation.features.home_screen

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import com.prayercompanion.shared.presentation.features.home_screen.components.HomeScreenContent
import com.prayercompanion.shared.presentation.utils.compose.LifecycleEvent
import com.prayercompanion.shared.presentation.utils.compose.OnLifecycleEvent

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    scaffoldState: ScaffoldState,
    checkLocationService: () -> Unit
) {

    OnLifecycleEvent { event ->
        when (event) {
            LifecycleEvent.ON_START -> {
                viewModel.onStart()
                checkLocationService()
            }

            LifecycleEvent.ON_PAUSE -> {
                viewModel.onPause()
            }
        }
    }


    val (currentPrayer, nextPrayer) = viewModel.headerState.currentAndNextPrayer

    HomeScreenContent(
        currentPrayerInfo = currentPrayer,
        nextPrayerInfo = nextPrayer,
        statusesOverview = viewModel.headerState.statusesOverview,
        durationUntilNextPrayer = viewModel.durationUntilNextPrayer,
        selectedDate = viewModel.state.selectedDate,
        selectedDayPrayersInfo = viewModel.state.selectedDayPrayersInfo,
        onPrayedNowClicked = viewModel::onPrayedNowClicked,
        onPreviousDayButtonClicked = viewModel::onPreviousDayButtonClicked,
        onNextDayButtonClicked = viewModel::onNextDayButtonClicked,
        onStatusSelected = viewModel::onStatusSelected,
        onStatusOverviewBarClicked = viewModel::onStatusOverviewBarClicked,
        onIshaStatusesPeriodsExplanationClicked = viewModel::onIshaStatusesPeriodsExplanationClicked
    )
}