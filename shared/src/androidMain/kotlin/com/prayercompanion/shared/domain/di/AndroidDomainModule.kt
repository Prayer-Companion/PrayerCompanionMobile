package com.prayercompanion.shared.domain.di

import com.prayercompanion.shared.data.local.system.LocationRepositoryImpl
import com.prayercompanion.shared.domain.repositories.LocationRepository
import com.prayercompanion.shared.domain.usecases.GetAppLanguage
import com.prayercompanion.shared.domain.usecases.IsConnectedToInternet
import com.prayercompanion.shared.domain.usecases.SetAppLanguage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidDomainModule = module {
    singleOf(::LocationRepositoryImpl) { bind<LocationRepository>() }
    singleOf(::GetAppLanguage)
    singleOf(::SetAppLanguage)
    singleOf(::IsConnectedToInternet)
}