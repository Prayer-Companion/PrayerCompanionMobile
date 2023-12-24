package com.prayercompanion.shared.domain.di

import com.prayercompanion.shared.data.local.system.AppLocationManagerImpl
import com.prayercompanion.shared.data.system.AppLocationManager
import com.prayercompanion.shared.domain.usecases.GetAppLanguage
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.domain.usecases.SetAppLanguage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidDomainModule = module {
    singleOf(::AppLocationManagerImpl) { bind<AppLocationManager>() }
    singleOf(::GetAppLanguage)
    singleOf(::SetAppLanguage)
    singleOf(::IsConnectedToInternet)
}