package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent

@Preview
@Composable
fun PermissionsRequestScreen(
    navigate: (UiEvent.Navigate) -> Unit = {},
) {

    val spacing = LocalSpacing.current

    val locationPermissionContract = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // TODO: Handle the rejection case
        if (permissions.all { it.value }) {
            navigate(UiEvent.Navigate(Route.Home))
        }
    }

    PrayerCompanionAndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            Box(
                modifier = Modifier
                    .padding(spacing.spaceLarge)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.location_permission_request_explanation),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.onPrimary,
                        shape = RoundedCornerShape(topEnd = 50.dp, topStart = 50.dp)
                    )
                    .padding(vertical = spacing.spaceExtraLarge, horizontal = spacing.spaceLarge),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 45.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(50.dp),
                    onClick = {
                        locationPermissionContract.launch(AppLocationManager.permissions)
                    }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(spacing.spaceSmall))
                        Text(text = stringResource(id = R.string.location_permission_request_cta))
                    }
                }
            }
        }
    }

}