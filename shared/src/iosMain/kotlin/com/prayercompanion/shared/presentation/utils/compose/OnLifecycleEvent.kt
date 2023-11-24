package com.prayercompanion.shared.presentation.utils.compose

import androidx.compose.runtime.Composable

@Composable
actual fun OnLifecycleEvent(onEvent: (event: LifecycleEvent) -> Unit) {
    onEvent(LifecycleEvent.ON_START)
    return
    TODO()
}