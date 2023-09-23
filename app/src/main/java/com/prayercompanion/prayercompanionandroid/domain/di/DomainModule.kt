package com.prayercompanion.prayercompanionandroid.domain.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.usecases.AccountSignIn
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.IsConnectedToInternet
import com.prayercompanion.prayercompanionandroid.domain.usecases.SetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDailyPrayersCombo
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDayPrayers
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetDayPrayersFlow
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetPrayerStatusRanges
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.GetStatusesOverView
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.SetPrayerStatusByDateTime
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.UpdatePrayerStatus
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.AddMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.EditMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.GetFullQuranWithMemorized
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.GetNextQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.LoadAndSaveQuranMemorizedChapters
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.LoadQuranReadingSuggestions
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.MarkQuranSectionAsRead
import com.prayercompanion.prayercompanionandroid.domain.usecases.quran.RemoveMemorizedChapterAyat
import com.prayercompanion.prayercompanionandroid.domain.usecases.settings.GetIsPauseMediaEnabled
import com.prayercompanion.prayercompanionandroid.domain.usecases.settings.SetPauseMediaEnabled
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManager
import com.prayercompanion.prayercompanionandroid.domain.utils.AppLocationManagerImpl
import com.prayercompanion.prayercompanionandroid.domain.utils.MediaController
import com.prayercompanion.prayercompanionandroid.domain.utils.OrientationSensor
import com.prayercompanion.prayercompanionandroid.domain.utils.PermissionsManager
import com.prayercompanion.prayercompanionandroid.domain.utils.ScheduleDailyPrayersWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::AppLocationManagerImpl) { bind<AppLocationManager>() }
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        GoogleSignIn.getClient(androidContext(), gso)
    }
    singleOf(::GetDailyPrayersCombo)
    singleOf(::GetDayPrayers)
    singleOf(::GetDayPrayersFlow)
    singleOf(::GetPrayerStatusRanges)
    singleOf(::GetStatusesOverView)
    singleOf(::SetPrayerStatusByDateTime)
    singleOf(::UpdatePrayerStatus)
    singleOf(::AddMemorizedChapterAyat)
    singleOf(::EditMemorizedChapterAyat)
    singleOf(::GetFullQuranWithMemorized)
    singleOf(::GetNextQuranReadingSections)
    singleOf(::LoadAndSaveQuranMemorizedChapters)
    singleOf(::LoadQuranReadingSuggestions)
    singleOf(::MarkQuranSectionAsRead)
    singleOf(::RemoveMemorizedChapterAyat)
    singleOf(::GetIsPauseMediaEnabled)
    singleOf(::SetPauseMediaEnabled)
    singleOf(::AccountSignIn)
    singleOf(::GetAppLanguage)
    singleOf(::IsConnectedToInternet)
    singleOf(::SetAppLanguage)
    singleOf(::MediaController)
    singleOf(::PermissionsManager)
    singleOf(::OrientationSensor)
    singleOf(::ScheduleDailyPrayersWorker)
}