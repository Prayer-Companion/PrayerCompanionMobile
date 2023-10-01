package com.prayercompanion.shared.domain.usecases

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.prayercompanion.shared.domain.models.app.AppLanguage

actual class SetAppLanguage {

    actual fun call(appLanguage: AppLanguage) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(appLanguage.code)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}