package com.prayercompanion.prayercompanionandroid.data.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.tasks.Tasks
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.data.local.db.PrayerCompanionDatabase
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.MemorizedQuranChapterDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.repositories.PrayersRepositoryImpl
import com.prayercompanion.prayercompanionandroid.data.repositories.QuranRepositoryImpl
import com.prayercompanion.prayercompanionandroid.data.utils.AndroidPrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.data.utils.AuthenticationHelperImpl
import com.prayercompanion.prayercompanionandroid.data.utils.TrackerImpl
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import com.prayercompanion.prayercompanionandroid.domain.utils.AuthenticationHelper
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.Tracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class DataModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {

        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
        return HttpClient(OkHttp) {
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

    @Provides
    @Singleton
    fun providePrayerCompanionApi(client: HttpClient): PrayerCompanionApi {
        return PrayerCompanionApi(client)
    }

    @Provides
    @Singleton
    internal fun providePrayersRepository(
        usecase: PrayersRepositoryImpl
    ): PrayersRepository = usecase

    @Provides
    @Singleton
    internal fun provideQuranRepository(
        usecase: QuranRepositoryImpl
    ): QuranRepository = usecase

    @Provides
    @Singleton
    internal fun provideAlarmScheduler(
        util: AndroidPrayersAlarmScheduler
    ): PrayersAlarmScheduler = util

    @Provides
    @Singleton
    fun providePrayerCompanionDatabase(@ApplicationContext applicationContext: Context): PrayerCompanionDatabase {
        return Room
            .databaseBuilder(
                applicationContext,
                PrayerCompanionDatabase::class.java, "prayer-companion"
            ).build()
    }

    @Provides
    @Singleton
    fun providePrayersInfoDao(prayerCompanionDatabase: PrayerCompanionDatabase): PrayersInfoDao {
        return prayerCompanionDatabase.prayersInfoDao()
    }

    @Provides
    @Singleton
    fun provideQuranReadingSectionsDao(prayerCompanionDatabase: PrayerCompanionDatabase): QuranReadingSectionsDao {
        return prayerCompanionDatabase.quranReadingSectionsDao()
    }

    @Provides
    @Singleton
    fun provideMemorizedQuranChapterDao(prayerCompanionDatabase: PrayerCompanionDatabase): MemorizedQuranChapterDao {
        return prayerCompanionDatabase.memorizedQuranChapterDao()
    }

    @Provides
    @Singleton
    internal fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    internal fun provideAuthenticationHelper(usecase: AuthenticationHelperImpl): AuthenticationHelper =
        usecase

    @Provides
    @Singleton
    internal fun provideTracker(usecase: TrackerImpl): Tracker = usecase
}