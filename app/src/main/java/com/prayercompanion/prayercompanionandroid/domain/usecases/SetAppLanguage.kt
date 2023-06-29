package com.prayercompanion.prayercompanionandroid.domain.usecases

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import javax.inject.Inject

class SetAppLanguage @Inject constructor(
    private val dataStoresRepo: DataStoresRepo
) {

    suspend fun call(appLanguage: AppLanguage) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(appLanguage.code)
        AppCompatDelegate.setApplicationLocales(appLocale)
        dataStoresRepo.appPreferencesDataStore.updateData {
            it.copy(appLanguage = appLanguage)
        }
    }
}