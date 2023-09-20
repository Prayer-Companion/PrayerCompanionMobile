package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Preview
@Composable
fun SplashScreen(
    uiEvents: Flow<UiEvent> = emptyFlow(),
    navigate: (UiEvent.Navigate, NavOptionsBuilder.() -> Unit) -> Unit = { _, _ -> }
) {

    LaunchedEffect(key1 = uiEvents) {
        uiEvents.collect {
            when (it) {
                is UiEvent.Navigate -> navigate(it) {
                    popUpTo(Route.SplashScreen.name) {
                        inclusive = true
                    }
                }

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