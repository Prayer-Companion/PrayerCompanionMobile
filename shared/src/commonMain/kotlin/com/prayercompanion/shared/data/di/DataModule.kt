package com.prayercompanion.shared.data.di

import app.cash.sqldelight.db.SqlDriver
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.shared.BuildConfigs
import com.prayercompanion.shared.data.local.DatabaseDriverFactory
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
import com.prayercompanion.shared.data.utils.TrackerImpl
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.domain.repositories.PrayersRepository
import com.prayercompanion.shared.domain.repositories.QuranRepository
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {

    single {
        HttpClient {
            val authRepo: AuthenticationRepository = get()

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            // set default request parameters
            defaultRequest {
                url(BuildConfigs.PRAYER_COMPANION_API_BASE_URL)

                runBlocking {
                    val token = authRepo.getIdToken() ?: return@runBlocking
                    header("Authorization", "Bearer $token")
                }
            }

            install(ContentNegotiation) {
                json()
            }
        }
    }

    single {
        val factory: DatabaseDriverFactory = get()
        val driver: SqlDriver = factory.createDriver()
        PrayerCompanionDatabase(driver)
    }

    singleOf(::DatabaseDriverFactory)
    singleOf(::PrayerCompanionApi)
    singleOf(::PrayersRepositoryImpl) { bind<PrayersRepository>() }
    singleOf(::QuranRepositoryImpl) { bind<QuranRepository>() }
    singleOf(::PrayersInfoDaoImpl) { bind<PrayersInfoDao>() }
    singleOf(::QuranReadingSectionsDaoImpl) { bind<QuranReadingSectionsDao>() }
    singleOf(::MemorizedQuranChapterDaoImpl) { bind<MemorizedQuranChapterDao>() }
    singleOf(::Assets)
    singleOf(::DataStoresRepo)
    singleOf(::TrackerImpl) { bind<Tracker>() }
}