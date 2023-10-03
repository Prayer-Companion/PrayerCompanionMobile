package com.prayercompanion.shared.domain.di

import com.prayercompanion.shared.domain.usecases.AccountSignIn
import com.prayercompanion.shared.domain.usecases.prayers.GetDailyPrayersCombo
import com.prayercompanion.shared.domain.usecases.prayers.GetDayPrayers
import com.prayercompanion.shared.domain.usecases.prayers.GetDayPrayersFlow
import com.prayercompanion.shared.domain.usecases.prayers.GetPrayerStatusRanges
import com.prayercompanion.shared.domain.usecases.prayers.GetStatusesOverView
import com.prayercompanion.shared.domain.usecases.prayers.SetPrayerStatusByDateTime
import com.prayercompanion.shared.domain.usecases.prayers.UpdatePrayerStatus
import com.prayercompanion.shared.domain.usecases.quran.AddMemorizedChapterAyat
import com.prayercompanion.shared.domain.usecases.quran.EditMemorizedChapterAyat
import com.prayercompanion.shared.domain.usecases.quran.GetFullQuranWithMemorized
import com.prayercompanion.shared.domain.usecases.quran.GetNextQuranReadingSections
import com.prayercompanion.shared.domain.usecases.quran.LoadAndSaveQuranMemorizedChapters
import com.prayercompanion.shared.domain.usecases.quran.LoadQuranReadingSuggestions
import com.prayercompanion.shared.domain.usecases.quran.MarkQuranSectionAsRead
import com.prayercompanion.shared.domain.usecases.quran.RemoveMemorizedChapterAyat
import com.prayercompanion.shared.domain.usecases.settings.GetIsPauseMediaEnabled
import com.prayercompanion.shared.domain.usecases.settings.SetPauseMediaEnabled
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
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
}