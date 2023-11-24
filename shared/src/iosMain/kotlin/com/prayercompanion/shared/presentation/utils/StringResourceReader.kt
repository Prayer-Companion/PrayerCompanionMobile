package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.stringWithFormat

actual class StringResourceReader {

    actual fun read(stringRes: StringRes): String {
        return stringRes.id.localized()
    }
}

@Composable
actual fun stringResource(id: StringRes, args: List<Any>): String {
    return NSString.stringWithFormat(id.id.localized())//, *args.toTypedArray()) todo ios: check how to use arguments
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