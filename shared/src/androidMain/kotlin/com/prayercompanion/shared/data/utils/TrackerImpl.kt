package com.prayercompanion.shared.data.utils

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker

actual class TrackerImpl constructor(
    private val firebaseAnalytics: FirebaseAnalytics
): Tracker {

    actual override fun setAppLanguage(languageCode: String) {
        firebaseAnalytics.setUserProperty("app_language", languageCode)
    }

    actual override fun trackScreenView(screenName: String, screenClass: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    actual override fun trackButtonClicked(button: TrackedButtons) {
        firebaseAnalytics.logEvent("button_clicked") {
            param("name", button.name.lowercase())
        }
    }

    actual override fun trackStatusSelect() {
        firebaseAnalytics.logEvent("prayer_status_selected", null)
    }

    actual override fun trackQuranChapterAdd() {
        firebaseAnalytics.logEvent("quran_chapter_add", null)
    }

    actual override fun trackQuranChapterRemove() {
        firebaseAnalytics.logEvent("quran_chapter_remove", null)
    }

    actual override fun trackLogin() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
    }

    actual override fun trackLocationPermissionResult(granted: Boolean) {
        val suffix = if (granted) "granted" else "denied"
        firebaseAnalytics.logEvent("location_permission_${suffix}", null)
    }

    actual override fun trackNotificationPermissionResult(granted: Boolean) {
        val suffix = if (granted) "granted" else "denied"
        firebaseAnalytics.logEvent("notification_permission_${suffix}", null)
    }

}