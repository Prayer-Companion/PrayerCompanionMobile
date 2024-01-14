package com.prayercompanion.shared.data.utils

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.domain.utils.tracking.TrackingEventNames
import com.prayercompanion.shared.domain.utils.tracking.TrackingParameterNames

actual class TrackerImpl constructor(
    context: Context
) : Tracker {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    actual override fun setAppLanguage(languageCode: String) {
        firebaseAnalytics.setUserProperty(TrackingEventNames.APP_LANGUAGE, languageCode)
    }

    actual override fun trackScreenView(screenName: String, screenClass: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    actual override fun trackButtonClicked(button: TrackedButtons) {
        firebaseAnalytics.logEvent(TrackingEventNames.BUTTON_CLICKED) {
            param(TrackingParameterNames.BUTTON_NAME, button.name.lowercase())
        }
    }

    actual override fun trackStatusSelect() {
        firebaseAnalytics.logEvent(TrackingEventNames.PRAYER_STATUS_SELECTED, null)
    }

    actual override fun trackQuranChapterAdd() {
        firebaseAnalytics.logEvent(TrackingEventNames.QURAN_CHAPTER_ADD, null)
    }

    actual override fun trackQuranChapterRemove() {
        firebaseAnalytics.logEvent(TrackingEventNames.QURAN_CHAPTER_REMOVE, null)
    }

    actual override fun trackLogin() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
    }

    actual override fun trackLocationPermissionResult(granted: Boolean) {
        val suffix = if (granted) {
            TrackingEventNames.GRANTED_SUFFIX
        } else {
            TrackingEventNames.DENIED_SUFFIX
        }

        firebaseAnalytics.logEvent(TrackingEventNames.LOCATION_PERMISSION_PREFIX + suffix, null)
    }

    actual override fun trackNotificationPermissionResult(granted: Boolean) {
        val suffix = if (granted) {
            TrackingEventNames.GRANTED_SUFFIX
        } else {
            TrackingEventNames.DENIED_SUFFIX
        }

        firebaseAnalytics.logEvent(TrackingEventNames.NOTIFICATION_PERMISSION_PREFIX + suffix, null)
    }

}