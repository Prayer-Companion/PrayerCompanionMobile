package com.prayercompanion.shared.presentation.utils.compose

import androidx.compose.runtime.Composable

@Composable
expect fun OnLifecycleEvent(onEvent: (event: LifecycleEvent) -> Unit)