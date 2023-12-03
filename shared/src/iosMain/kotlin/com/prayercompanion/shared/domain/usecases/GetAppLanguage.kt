package com.prayercompanion.shared.domain.usecases

import com.prayercompanion.shared.domain.models.app.AppLanguage

actual class GetAppLanguage {

    actual fun call(): AppLanguage {
        return AppLanguage.AR
        TODO()
    }
}
