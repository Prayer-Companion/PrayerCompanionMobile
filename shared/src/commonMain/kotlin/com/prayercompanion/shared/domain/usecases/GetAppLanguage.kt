package com.prayercompanion.shared.domain.usecases

import com.prayercompanion.shared.domain.models.app.AppLanguage

expect class GetAppLanguage {

    fun call(): AppLanguage
}
