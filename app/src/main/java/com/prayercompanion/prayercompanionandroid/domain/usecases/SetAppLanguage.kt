package com.prayercompanion.prayercompanionandroid.domain.usecases

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage

class SetAppLanguage {

    fun call(appLanguage: AppLanguage) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(appLanguage.code)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}