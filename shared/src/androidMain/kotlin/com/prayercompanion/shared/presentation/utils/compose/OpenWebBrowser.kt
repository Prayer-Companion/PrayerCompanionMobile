package com.prayercompanion.shared.presentation.utils.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun OpenWebBrowser(): (url: String) -> Unit {
    val context = LocalContext.current
    return { url ->
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        context.startActivity(browserIntent)
    }
}