package com.prayercompanion.shared.presentation.features.main.home_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.prayercompanion.shared.BottomNavItem
import com.prayercompanion.shared.presentation.components.InAppReview
import com.prayercompanion.shared.presentation.features.main.LocalScaffoldState
import com.prayercompanion.shared.presentation.features.main.home_screen.components.HomeScreenContent
import com.prayercompanion.shared.presentation.utils.StringResourceReader
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.asString
import com.prayercompanion.shared.presentation.utils.compose.LocationSettingsLauncher
import com.prayercompanion.shared.presentation.utils.createTabOptions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object HomeScreen : Tab, KoinComponent {

    private val stringResourceReader: StringResourceReader by inject()

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        LifecycleEffect(
            onStarted = {
                viewModel.onStart()
            },
            onDisposed = {
                viewModel.onPause()
            }
        )

        HomeScreen(viewModel, stringResourceReader)
    }


    override val options: TabOptions
        @Composable
        get() = createTabOptions(BottomNavItem.Home)
}

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    stringResourceReader: StringResourceReader
) {
    val inAppReview = InAppReview(
        onFailure = viewModel::onInAppReviewFailed,
        onCompleted = viewModel::onInAppReviewCompleted
    )
    val launchLocationSettingsDialog = LocationSettingsLauncher {
        viewModel.onLocationSettingsResult(it)
    }

    val uriHandler = LocalUriHandler.current
    val scaffoldState = LocalScaffoldState.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect {
            when (it) {
                is UiEvent.ShowEnableLocationSettingsDialog -> {
                    launchLocationSettingsDialog.invoke()
                }

                is UiEvent.ShowMessage -> {
                    scaffoldState
                        .snackbarHostState
                        .showSnackbar(it.message.asString(stringResourceReader))
                }

                is UiEvent.ShowRateTheAppPopup -> {
                    inAppReview.show()
                }

                is UiEvent.OpenWebUrl -> {
                    uriHandler.openUri(it.url)
                }

                else -> Unit
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