package com.prayercompanion.shared.domain.usecases

import com.prayercompanion.shared.domain.models.app.AppLanguage
import platform.Foundation.NSBundle

actual class GetAppLanguage {

    actual fun call(): AppLanguage {
        val language = NSBundle.mainBundle.preferredLocalizations.firstOrNull() as String?
            ?: return AppLanguage.AR

        return AppLanguage.fromLanguageCode(language)
    }
}
