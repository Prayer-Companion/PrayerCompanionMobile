package com.prayercompanion.prayercompanionandroid.domain.usecases

import androidx.appcompat.app.AppCompatDelegate
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import javax.inject.Inject

class GetAppLanguage @Inject constructor() {

    fun call(): AppLanguage? {
        val language = AppCompatDelegate
            .getApplicationLocales()
            .toLanguageTags()
            .takeIf { it.isNotEmpty() } ?: return null

        return AppLanguage.fromLanguageCode(language)
    }
}