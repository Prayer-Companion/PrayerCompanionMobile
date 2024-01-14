package com.prayercompanion.shared.data.utils

import cocoapods.FirebaseAnalytics.FIRAnalytics
import cocoapods.FirebaseAnalytics.kFIREventLogin
import cocoapods.FirebaseAnalytics.kFIREventScreenView
import cocoapods.FirebaseAnalytics.kFIRParameterScreenClass
import cocoapods.FirebaseAnalytics.kFIRParameterScreenName
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.domain.utils.tracking.TrackingEventNames
import com.prayercompanion.shared.domain.utils.tracking.TrackingParameterNames

actual class TrackerImpl : Tracker {

    actual override fun setAppLanguage(languageCode: String) {
        FIRAnalytics.setUserPropertyString(TrackingEventNames.APP_LANGUAGE, languageCode)
    }

    actual override fun trackScreenView(screenName: String, screenClass: String) {
        FIRAnalytics.logEventWithName(
            name = kFIREventScreenView!!, parameters = mapOf(
                kFIRParameterScreenName to screenName,
                kFIRParameterScreenClass to screenClass
            )
        )
    }

    actual override fun trackButtonClicked(button: TrackedButtons) {
        FIRAnalytics.logEventWithName(
            name = TrackingEventNames.BUTTON_CLICKED, parameters = mapOf(
                TrackingParameterNames.BUTTON_NAME to button.name.lowercase()
            )
        )
    }

    actual override fun trackStatusSelect() {
        FIRAnalytics.logEventWithName(
            name = TrackingEventNames.PRAYER_STATUS_SELECTED,
            parameters = null
        )
    }

    actual override fun trackQuranChapterAdd() {
        FIRAnalytics.logEventWithName(
            name = TrackingEventNames.QURAN_CHAPTER_ADD,
            parameters = null
        )
    }

    actual override fun trackQuranChapterRemove() {
        FIRAnalytics.logEventWithName(
            name = TrackingEventNames.QURAN_CHAPTER_REMOVE,
            parameters = null
        )
    }

    actual override fun trackLogin() {
        FIRAnalytics.logEventWithName(name = kFIREventLogin!!, parameters = null)
    }

    actual override fun trackLocationPermissionResult(granted: Boolean) {
        val suffix = if (granted) {
            TrackingEventNames.GRANTED_SUFFIX
        } else {
            TrackingEventNames.DENIED_SUFFIX
        }

        FIRAnalytics.logEventWithName(
            name = TrackingEventNames.LOCATION_PERMISSION_PREFIX + suffix,
            parameters = null
        )
    }

    actual override fun trackNotificationPermissionResult(granted: Boolean) {
        val suffix = if (granted) {
            TrackingEventNames.GRANTED_SUFFIX
        } else {
            TrackingEventNames.DENIED_SUFFIX
        }

        FIRAnalytics.logEventWithName(
            name = TrackingEventNames.NOTIFICATION_PERMISSION_PREFIX + suffix,
            parameters = null
        )
    }
}
