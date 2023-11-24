package com.prayercompanion.shared.data.di

import com.prayercompanion.shared.data.local.assets.Assets
import com.prayercompanion.shared.data.local.db.daos.MemorizedQuranChapterDao
import com.prayercompanion.shared.data.local.db.daos.MemorizedQuranChapterDaoImpl
import com.prayercompanion.shared.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.shared.data.local.db.daos.PrayersInfoDaoImpl
import com.prayercompanion.shared.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.shared.data.local.db.daos.QuranReadingSectionsDaoImpl
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.data.remote.PrayerCompanionApi
import com.prayercompanion.shared.data.repositories.PrayersRepositoryImpl
import com.prayercompanion.shared.data.repositories.QuranRepositoryImpl
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.repositories.QuranRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::PrayerCompanionApi)
    singleOf(::PrayersRepositoryImpl) { bind<PrayersRepository>() }
    singleOf(::QuranRepositoryImpl) { bind<QuranRepository>() }
    singleOf(::PrayersInfoDaoImpl) { bind<PrayersInfoDao>() }
    singleOf(::QuranReadingSectionsDaoImpl) { bind<QuranReadingSectionsDao>() }
    singleOf(::MemorizedQuranChapterDaoImpl) { bind<MemorizedQuranChapterDao>() }
    singleOf(::Assets)
    singleOf(::DataStoresRepo)
}