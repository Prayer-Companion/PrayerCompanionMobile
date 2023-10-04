package com.prayercompanion.prayercompanionandroid.presentation.features.home_screen

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.components.HomeScreenContent
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.compose.OnLifecycleEvent
import com.prayercompanion.prayercompanionandroid.presentation.utils.showToast
import logcat.asLog
import logcat.logcat

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
        fun checkLocationService() {
            val intervalForLocationUpdateInMillis = 10000L

            val locationRequest = LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalForLocationUpdateInMillis)
                .build()

            val locationSettingsRequest = LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
                .build()

            val client = LocationServices.getSettingsClient(context)

            client.checkLocationSettings(locationSettingsRequest)
                .addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            val intentSenderRequest = IntentSenderRequest
                                .Builder(exception.resolution.intentSender)
                                .build()

                            locationSettingsLauncher.launch(intentSenderRequest)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            logcat("HomeScreen") { sendEx.asLog() }
                        }
                    }
                }
        }

        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.onStart()
                checkLocationService()
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