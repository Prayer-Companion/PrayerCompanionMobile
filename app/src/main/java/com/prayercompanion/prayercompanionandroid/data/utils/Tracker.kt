package com.prayercompanion.prayercompanionandroid.data.utils

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.TrackedButtons
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.Tracker
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import javax.inject.Inject

class TrackerImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    getAppLanguage: GetAppLanguage
): Tracker {

    init {
        val languageCode = getAppLanguage.call().code
        firebaseAnalytics.setUserProperty("app_language", languageCode)
    }

    override fun trackScreenView(route: Route, screenClass: String) {
        val screenName = route.routeName.substringBefore("/")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    override fun trackButtonClicked(button: TrackedButtons) {
        firebaseAnalytics.logEvent("button_clicked") {
            param("name", button.name.lowercase())
        }
    }

    override fun trackStatusSelect() {
        firebaseAnalytics.logEvent("prayer_status_selected", null)
    }

    override fun trackQuranChapterAdd() {
        firebaseAnalytics.logEvent("quran_chapter_add", null)
    }

    override fun trackQuranChapterRemove() {
        firebaseAnalytics.logEvent("quran_chapter_remove", null)
    }

    override fun trackLogin() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
    }

    override fun trackLocationPermissionResult(granted: Boolean) {
        val suffix = if (granted) "granted" else "denied"
        firebaseAnalytics.logEvent("location_permission_${suffix}", null)
    }

    override fun trackNotificationPermissionResult(granted: Boolean) {
        val suffix = if (granted) "granted" else "denied"
        firebaseAnalytics.logEvent("notification_permission_${suffix}", null)
    }

}