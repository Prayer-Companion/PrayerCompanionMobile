package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.prayercompanion.prayercompanionandroid.MainActivity
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent

@Preview
@Composable
fun PermissionsRequestScreen(
    navigate: (UiEvent.Navigate, NavOptionsBuilder.() -> Unit) -> Unit = { _, _ -> },
) {

    val spacing = LocalSpacing.current
    val activity = LocalContext.current as Activity

    fun navigateToHome() {
        navigate(UiEvent.Navigate(Route.Home)) {
            popUpTo(Route.PermissionsRequests.name) {
                inclusive = true
            }
        }
    }


    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                navigateToHome()
            }
        }
    )

    fun requestTurnOnLocation(activity: Activity) {
        val intervalForLocationUpdateInMillis = 10000L

        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalForLocationUpdateInMillis)
            .build()

        val locationSettingsRequest =
            LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
                .build()

        val client = LocationServices.getSettingsClient(activity)

        val task = client.checkLocationSettings(locationSettingsRequest)

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            navigateToHome()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(activity, requestCode)
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                    locationSettingsLauncher.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }

    val locationPermissionDeclined = remember { mutableStateOf(false) }

    val locationPermissionContract = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // TODO: Handle the rejection case
        if (permissions.all { it.value }) {
            requestTurnOnLocation(activity)
        } else {
            locationPermissionDeclined.value = true
        }
    }

    PrayerCompanionAndroidTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                            .background(MaterialTheme.colors.primary, RoundedCornerShape(100))
                            .padding(30.dp),
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = spacing.spaceLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.location_permission_request_title),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceLarge))
                    Text(
                        text = stringResource(id = R.string.location_permission_request_explanation),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.secondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topEnd = 50.dp, topStart = 50.dp)
                    )
                    .padding(
                        bottom = spacing.spaceExtraLarge,
                        top = spacing.spaceLarge,
                        start = spacing.spaceLarge,
                        end = spacing.spaceLarge,
                    )
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 45.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.onPrimary),
                    shape = RoundedCornerShape(50.dp),
                    onClick = {
                        locationPermissionContract.launch(AppLocationManager.permissions)
                    }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceSmall))
                        Text(
                            text = stringResource(id = R.string.location_permission_request_cta),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }

}



