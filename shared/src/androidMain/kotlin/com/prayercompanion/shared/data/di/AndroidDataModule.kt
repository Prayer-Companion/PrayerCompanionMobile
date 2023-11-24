package com.prayercompanion.shared.data.di

import app.cash.sqldelight.db.SqlDriver
import com.google.android.gms.tasks.Tasks
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.shared.BuildConfig
import com.prayercompanion.shared.SQLDelightDriverFactory
import com.prayercompanion.shared.data.local.assets.AssetsReader
import com.prayercompanion.shared.data.utils.TrackerImpl
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

val androidDataModule = module {
    single {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        HttpClient(OkHttp) {
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
                json()
            }
        }
    }

    single {
        val driver: SqlDriver = SQLDelightDriverFactory(androidContext()).createDriver()
        PrayerCompanionDatabase(driver)
    }
    single { FirebaseAnalytics.getInstance(androidContext()) }
    singleOf(::TrackerImpl) { bind<Tracker>() }
    singleOf(::AssetsReader)
}