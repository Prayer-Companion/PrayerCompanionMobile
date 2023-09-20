package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components.HomeScreenContent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.compose.OnLifecycleEvent
import com.prayercompanion.prayercompanionandroid.showToast

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    scaffoldState: ScaffoldState,
    activity: Activity
) {
    val context = LocalContext.current

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            viewModel.onLocationSettingsResult(result.resultCode == Activity.RESULT_OK)
        }
    )

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.onStart()
            }

            Lifecycle.Event.ON_PAUSE -> {
                viewModel.onPause()
            }

            else -> Unit
        }
    }

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

                is UiEvent.ShowRateTheAppPopup -> {
                    val manager = ReviewManagerFactory.create(context)
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val reviewInfo = task.result
                            manager.launchReviewFlow(activity, reviewInfo)
                                .addOnSuccessListener {
                                    if (BuildConfig.DEBUG) {
                                        context.showToast("In app review shown")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    if (BuildConfig.DEBUG) {
                                        context.showToast("launchReviewFlow failed")
                                    }
                                    FirebaseCrashlytics.getInstance().recordException(exception)
                                }
                        } else {
                            if (BuildConfig.DEBUG) {
                                context.showToast("requestReviewFlow failed")
                            }
                            task.exception?.let { exception ->
                                FirebaseCrashlytics.getInstance().recordException(exception)
                            }
                        }
                    }
                }

                is UiEvent.OpenWebUrl -> {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(it.url)
                    )
                    context.startActivity(browserIntent)
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