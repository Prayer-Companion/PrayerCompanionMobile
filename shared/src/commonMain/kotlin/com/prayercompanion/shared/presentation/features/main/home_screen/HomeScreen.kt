package com.prayercompanion.shared.presentation.features.main.home_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.prayercompanion.shared.BottomNavItem
import com.prayercompanion.shared.presentation.features.main.LocalScaffoldState
import com.prayercompanion.shared.presentation.features.main.home_screen.components.HomeScreenContent
import com.prayercompanion.shared.presentation.utils.StringResourceReader
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.asString
import com.prayercompanion.shared.presentation.utils.compose.LifecycleEvent
import com.prayercompanion.shared.presentation.utils.compose.LocationSettingsLauncher
import com.prayercompanion.shared.presentation.utils.compose.OnLifecycleEvent
import com.prayercompanion.shared.presentation.utils.createTabOptions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object HomeScreen : Tab, KoinComponent {

    private val stringResourceReader: StringResourceReader by inject()

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()

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
    val launchLocationSettingsDialog = LocationSettingsLauncher {
        viewModel.onLocationSettingsResult(it)
    }

    val uriHandler = LocalUriHandler.current
    val scaffoldState = LocalScaffoldState.current

    OnLifecycleEvent { event ->
        when (event) {
            LifecycleEvent.ON_START -> {
                viewModel.onStart()
            }

            LifecycleEvent.ON_PAUSE -> {
                viewModel.onPause()
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect {
            when (it) {
                is UiEvent.ShowEnableLocationSettingsDialog -> {
                    launchLocationSettingsDialog.invoke()
                }

                is UiEvent.ShowErrorSnackBar -> {
                    scaffoldState
                        .snackbarHostState
                        .showSnackbar(it.errorMessage.asString(stringResourceReader))
                }

                is UiEvent.ShowRateTheAppPopup -> {
//                    todo implement in app review kmm
//                    val manager = ReviewManagerFactory.create(context)
//                    val request = manager.requestReviewFlow()
//                    request.addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val reviewInfo = task.result
//                            manager.launchReviewFlow(activity, reviewInfo)
//                                .addOnSuccessListener {
//                                    if (BuildConfig.DEBUG) {
//                                        context.showToast("In app review shown")
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    if (BuildConfig.DEBUG) {
//                                        context.showToast("launchReviewFlow failed")
//                                    }
//                                    FirebaseCrashlytics.getInstance().recordException(exception)
//                                }
//                        } else {
//                            if (BuildConfig.DEBUG) {
//                                context.showToast("requestReviewFlow failed")
//                            }
//                            task.exception?.let { exception ->
//                                FirebaseCrashlytics.getInstance().recordException(exception)
//                            }
//                        }
//                    }
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