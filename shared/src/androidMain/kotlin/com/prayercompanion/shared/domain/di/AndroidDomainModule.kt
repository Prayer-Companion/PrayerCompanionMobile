package com.prayercompanion.shared.domain.di

import com.prayercompanion.shared.domain.usecases.GetAppLanguage
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.domain.usecases.SetAppLanguage
import com.prayercompanion.shared.domain.utils.AppLocationManager
import com.prayercompanion.shared.domain.utils.AppLocationManagerImpl
import com.prayercompanion.shared.domain.utils.PermissionsManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidDomainModule = module {
    singleOf(::AppLocationManagerImpl) { bind<AppLocationManager>() }
    singleOf(::GetAppLanguage)
    singleOf(::IsConnectedToInternet)
    singleOf(::SetAppLanguage)
    singleOf(::PermissionsManager)
}