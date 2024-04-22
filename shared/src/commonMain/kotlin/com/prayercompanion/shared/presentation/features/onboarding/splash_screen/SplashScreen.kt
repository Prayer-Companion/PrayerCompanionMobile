package com.prayercompanion.shared.presentation.features.onboarding.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prayercompanion.shared.domain.utils.MokoPermissionsManager
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.toScreen
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.component.KoinComponent

object SplashScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SplashScreenViewModel>()

        val navigator = LocalNavigator.currentOrThrow
        SplashScreen(
            onAction = {
                viewModel.onAction(it)
            },
            uiEvents = viewModel.uiEvents,
            navigate = { event ->
                navigator.replaceAll(event.route.toScreen())
            }
        )
    }
}

@Composable
private fun SplashScreen(
    onAction: (SplashScreenAction) -> Unit,
    uiEvents: Flow<UiEvent> = emptyFlow(),
    navigate: (UiEvent.Navigate) -> Unit = {}
) {

    val permissionFactory = rememberPermissionsControllerFactory()
    val mokoPermissionsManager = MokoPermissionsManager(permissionFactory)
    BindEffect(mokoPermissionsManager.permissionsController)

    LaunchedEffect(key1 = Unit) {
        onAction.invoke(
            SplashScreenAction.OnStart(
                mokoPermissionsManager.isLocationPermissionGranted(),
                mokoPermissionsManager.isPushNotificationAllowed()
            )
        )
    }

    LaunchedEffect(key1 = uiEvents) {
        uiEvents.collect {
            when (it) {
                is UiEvent.Navigate -> navigate(it)

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}