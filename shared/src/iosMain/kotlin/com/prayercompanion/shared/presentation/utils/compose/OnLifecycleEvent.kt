package com.prayercompanion.shared.presentation.utils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun OnLifecycleEvent(onEvent: (event: LifecycleEvent) -> Unit) {
    LaunchedEffect(Unit) {
        onEvent(LifecycleEvent.ON_START)
    }
//    TODO()
}