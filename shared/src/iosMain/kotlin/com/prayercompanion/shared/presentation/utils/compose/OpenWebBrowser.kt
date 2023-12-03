package com.prayercompanion.shared.presentation.utils.compose

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun OpenWebBrowser(): (url: String) -> Unit {
    return openBrowser@{
        val url = NSURL.URLWithString(it) ?: return@openBrowser
        UIApplication.sharedApplication().openURL(url)
    }
}