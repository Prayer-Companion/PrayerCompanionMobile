package com.prayercompanion.shared.domain.usecases

import com.prayercompanion.shared.domain.models.app.AppLanguage

expect class SetAppLanguage {

    fun call(appLanguage: AppLanguage)
}