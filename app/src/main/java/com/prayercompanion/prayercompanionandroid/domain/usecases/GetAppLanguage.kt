package com.prayercompanion.prayercompanionandroid.domain.usecases

import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import logcat.logcat

class GetAppLanguage {

    fun call(): AppLanguage {

        val systemLanguage = Resources.getSystem().configuration.locales.takeIf { it.isEmpty.not() }?.get(0)?.language

        val appLanguage = AppCompatDelegate
            .getApplicationLocales()
            .toLanguageTags()
            .takeIf { it.isNotEmpty() }
            ?.substringBefore('-').also { logcat { "Language: $it" } }
            ?: systemLanguage

        return AppLanguage.fromLanguageCode(appLanguage)
    }
}