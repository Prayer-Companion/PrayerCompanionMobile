package com.prayercompanion.shared.presentation.utils.compose

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun LocationSettingsLauncher(onResult: (isResultOk: Boolean) -> Unit): () -> Unit {
    return Callback@{
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return@Callback
        val canOpen = UIApplication.sharedApplication.canOpenURL(url)

        if (canOpen) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}

/*
if let url = URL(string:UIApplication.openSettingsURLString)



{ if UIApplication.shared.canOpenURL(url)

{ UIApplication.shared.open(url, options: [:], completionHandler: nil)



}

}
* */