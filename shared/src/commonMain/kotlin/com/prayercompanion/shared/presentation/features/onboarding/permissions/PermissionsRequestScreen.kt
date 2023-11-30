package com.prayercompanion.shared.presentation.features.onboarding.permissions

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prayercompanion.shared.domain.utils.MokoPermissionsManager
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.stringResource
import com.prayercompanion.shared.presentation.utils.toScreen
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent

object PermissionsRequestScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<PermissionsRequestViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        PermissionsRequestScreen(
            navigate = { event ->
                navigator.replaceAll(event.route.toScreen())
            },
            uiState = viewModel.uiState,
            uiEvents = viewModel.uiEvents,
            onEvent = {
                viewModel.onEvent(it)
            }

        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PermissionsRequestScreen(
    navigate: (UiEvent.Navigate) -> Unit,
    uiState: PermissionsRequestUiState,
    uiEvents: Flow<UiEvent> = emptyFlow(),
    onEvent: (PermissionsRequestEvent) -> Unit = { },
) {

    val spacing = LocalSpacing.current

    val permissionFactory = rememberPermissionsControllerFactory()
    val mokoPermissionsManager = remember(permissionFactory) {
        MokoPermissionsManager(permissionFactory)
    }
    BindEffect(mokoPermissionsManager.permissionsController)
    val coroutineScope = rememberCoroutineScope()

    fun requestPermissions(permissions: List<Permission>) {
        coroutineScope.launch {
            // this doesn't have to be in an awaitAll block because each permission is asked on its own
            val permissionsResult = permissions.associateWith {
                mokoPermissionsManager.requestPermission(it)
            }
            onEvent(PermissionsRequestEvent.OnPermissionResult(permissionsResult))
        }
    }

    LaunchedEffect(key1 = Unit) {
        onEvent.invoke(
            PermissionsRequestEvent.OnStart(
                mokoPermissionsManager.isLocationPermissionGranted(),
                mokoPermissionsManager.isPushNotificationAllowed()
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        uiEvents.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    navigate(it)
                }

                is UiEvent.RequestPermissions -> {
                    requestPermissions(it.permissions)
                }

                is UiEvent.OpenAppSettings -> {
                    //todo open settings
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri: Uri = Uri.fromParts("package", context.packageName, null)
//                    intent.data = uri
//                    startActivity(context, intent, null)
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
                        painter = painterResource(uiState.icon),
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
                                text = stringResource(id = StringRes.skip),
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