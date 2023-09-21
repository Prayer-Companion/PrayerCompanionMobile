package com.prayercompanion.prayercompanionandroid.data.di

import app.cash.sqldelight.db.SqlDriver
import com.google.android.gms.tasks.Tasks
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.prayercompanionandroid.data.local.assets.AssetsReader
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.MemorizedQuranChapterDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.MemorizedQuranChapterDaoImpl
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDaoImpl
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDaoImpl
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.repositories.PrayersRepositoryImpl
import com.prayercompanion.prayercompanionandroid.data.repositories.QuranRepositoryImpl
import com.prayercompanion.prayercompanionandroid.data.utils.AndroidPrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.data.utils.AuthenticationHelperImpl
import com.prayercompanion.prayercompanionandroid.data.utils.TrackerImpl
import com.prayercompanion.prayercompanionandroid.data.utils.notifications.PrayersNotificationsService
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AuthenticationHelper
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.Tracker
import com.prayercompanion.shared.SQLDelightDriverFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.gson.gson
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        HttpClient(OkHttp) {
            // throw  for non-2xx responses
            expectSuccess = true

            engine {
                addInterceptor { chain ->
                    val request = chain.request()

                    val task = FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.getIdToken(false)

                    if (task != null) {
                        val token = runCatching {
                            val result = Tasks.await(task)
                            result.token
                        }.getOrNull()

                        if (token != null) {
                            val new = request.newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .build()

                            return@addInterceptor chain.proceed(new)
                        }
                    }

                    Response.Builder()
                        .code(HTTP_UNAUTHORIZED)
                        .body("".toResponseBody(null)) // Whatever body
                        .protocol(Protocol.HTTP_2)
                        .message("Couldn't fetch firebase auth token")
                        .request(chain.request())
                        .build()
                }
                if (BuildConfig.DEBUG) {
                    addInterceptor(logger)
                }
            }

            // set default request parameters
            defaultRequest {
                url(BuildConfig.PRAYER_COMPANION_API_BASE_URL)
            }

            install(ContentNegotiation) {
                gson()
            }
        }
    }
    single { FirebaseAnalytics.getInstance(androidContext()) }
    single<PrayersAlarmScheduler> { AndroidPrayersAlarmScheduler(androidContext(), get()) }

    single {
        val driver: SqlDriver = SQLDelightDriverFactory(androidContext()).createDriver()
        PrayerCompanionDatabase(driver)
    }

    singleOf(::PrayerCompanionApi)
    singleOf(::PrayersRepositoryImpl) { bind<PrayersRepository>() }
    singleOf(::QuranRepositoryImpl) { bind<QuranRepository>() }
    singleOf(::PrayersInfoDaoImpl) { bind<PrayersInfoDao>() }
    singleOf(::QuranReadingSectionsDaoImpl) { bind<QuranReadingSectionsDao>() }
    singleOf(::MemorizedQuranChapterDaoImpl) { bind<MemorizedQuranChapterDao>() }
    singleOf(::AuthenticationHelperImpl) { bind<AuthenticationHelper>() }
    singleOf(::TrackerImpl) { bind<Tracker>() }
    singleOf(::AssetsReader)
    singleOf(::PrayersNotificationsService)
    singleOf(::DataStoresRepo)
}