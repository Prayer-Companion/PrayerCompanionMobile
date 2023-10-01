package com.prayercompanion.shared.domain.usecases

import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.prayercompanion.shared.domain.models.app.AppLanguage
import com.prayercompanion.shared.presentation.log

actual class GetAppLanguage {

    actual fun call(): AppLanguage {

        val systemLanguage = Resources.getSystem().configuration.locales.takeIf { it.isEmpty.not() }
            ?.get(0)?.language

        val appLanguage = AppCompatDelegate.getApplicationLocales()
            .toLanguageTags()
            .takeIf { it.isNotEmpty() }
            ?.substringBefore('-').also { log { "Language: $it" } }
            ?: systemLanguage

        return AppLanguage.fromLanguageCode(appLanguage)
    }
}