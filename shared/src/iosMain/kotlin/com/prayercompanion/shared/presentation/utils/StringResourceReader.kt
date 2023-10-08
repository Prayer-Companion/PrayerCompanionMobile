package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable
import platform.Foundation.NSBundle
import platform.Foundation.NSURL


actual class StringResourceReader{

    actual fun read(stringRes: StringRes): String {
        return stringRes.id.localized()
    }
}

@Composable
actual fun stringResource(stringRes: StringRes): String  {
    return stringRes.id.localized()
}

fun String.localized(): String {
    val localizedString = NSBundle.mainBundle.localizedStringForKey(this, this, null)
    if (localizedString != this) return localizedString

    val baseResourcePath = NSBundle.mainBundle.pathForResource("Base", "lproj")
        ?.let { NSURL(fileURLWithPath = it) }
    val baseBundle = baseResourcePath?.let { NSBundle(it) }

    if (baseBundle != null) {
        val baseString = baseBundle.localizedStringForKey(this, this, null)
        if (baseString != this) return baseString
    }

    return this
}