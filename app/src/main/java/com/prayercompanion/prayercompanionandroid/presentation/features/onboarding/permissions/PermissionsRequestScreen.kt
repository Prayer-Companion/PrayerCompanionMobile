package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Preview
@Composable
fun PermissionsRequestScreen(
    navigate: (UiEvent.Navigate, NavOptionsBuilder.() -> Unit) -> Unit = { _, _ -> },
    uiState: PermissionsRequestUiState = PermissionsRequestUiState(),
    uiEvents: Flow<UiEvent> = emptyFlow(),
    onEvent: (PermissionsRequestEvent) -> Unit = { },
) {

    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val permissionContract = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        onEvent(PermissionsRequestEvent.OnPermissionResult(permissions))
    }

    LaunchedEffect(key1 = true) {
        uiEvents.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    navigate(it) {
                        popUpTo(Route.PermissionsRequests.name) {
                            inclusive = true
                        }
                    }
                }

                is UiEvent.RequestPermissions -> {
                    permissionContract.launch(it.permissions.toTypedArray())
                }

                is UiEvent.OpenAppSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    startActivity(context, intent, null)
                }

                else -> Unit
            }
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
                        painter = painterResource(id = uiState.icon),
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
                        text = uiState.title.asString(),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceLarge))
                    Text(
                        text = uiState.body.asString(),
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
                    .height(146.dp)
                    .padding(
                        bottom = spacing.spaceLarge,
                        top = spacing.spaceLarge,
                        start = spacing.spaceLarge,
                        end = spacing.spaceLarge,
                    )
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 45.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.onPrimary),
                        shape = RoundedCornerShape(50.dp),
                        onClick = {
                            onEvent(PermissionsRequestEvent.OnCTAClicked)
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
                                text = uiState.ctaText.asString(),
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }

                    if (uiState.skippable) {
                        TextButton(onClick = {
                            onEvent(PermissionsRequestEvent.OnSkipNotificationPermission)
                        }) {
                            Text(
                                text = stringResource(id = R.string.skip),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }
        }
    }

}