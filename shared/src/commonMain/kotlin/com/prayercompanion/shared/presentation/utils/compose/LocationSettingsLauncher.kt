package com.prayercompanion.shared.presentation.utils.compose

import androidx.compose.runtime.Composable

@Composable
expect fun LocationSettingsLauncher(onResult: (isResultOk: Boolean) -> Unit): () -> Unit