package com.prayercompanion.prayercompanionandroid.data.di

import android.content.Context
import androidx.room.Room
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.data.local.db.PrayerCompanionDatabase
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.remote.PrayerCompanionApi
import com.prayercompanion.prayercompanionandroid.data.repositories.PrayersRepositoryImpl
import com.prayercompanion.prayercompanionandroid.data.repositories.QuranRepositoryImpl
import com.prayercompanion.prayercompanionandroid.data.utils.AndroidPrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.domain.repositories.PrayersRepository
import com.prayercompanion.prayercompanionandroid.domain.repositories.QuranRepository
import com.prayercompanion.prayercompanionandroid.domain.usecases.UpdateAuthToken
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(updateAuthToken: UpdateAuthToken): OkHttpClient {

        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder()

        builder.addInterceptor { chain ->
            val request = chain.request()
            val new = request.newBuilder()
                .addHeader("Authorization", "Bearer ${Consts.userToken}")
                .build()
            chain.proceed(new)
        }

        builder.addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.code == HTTP_UNAUTHORIZED) {
                updateAuthToken.call(true)
            }

            response
        }

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logger)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun providePrayerCompanionApi(client: OkHttpClient): PrayerCompanionApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PRAYER_COMPANION_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
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

}