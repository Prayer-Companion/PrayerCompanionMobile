package com.prayercompanion.prayercompanionandroid.presentation.di

import android.app.Activity
import android.content.Context
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.presentation.utils.FeedbackUtils
import com.prayercompanion.prayercompanionandroid.presentation.utils.FeedbackUtilsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import io.doorbell.android.Doorbell

@Module
@InstallIn(ActivityComponent::class)
class PresentationModule {

    @Provides
    @ActivityScoped
    internal fun provideDoorbell(@ActivityContext context: Context): Doorbell {
        val activity = context as Activity
        return Doorbell(
            activity,
            BuildConfig.DOORBELL_ID.toLong(),
            BuildConfig.DOORBELL_PRIVATE_KEY,
        )
    }


    @Provides
    @ActivityScoped
    internal fun provideFeedbackUtils(feedbackUtils: FeedbackUtilsImpl): FeedbackUtils =
        feedbackUtils
}