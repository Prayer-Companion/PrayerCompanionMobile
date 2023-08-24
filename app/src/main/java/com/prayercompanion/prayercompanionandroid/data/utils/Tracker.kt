package com.prayercompanion.prayercompanionandroid.data.utils

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetAppLanguage
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import javax.inject.Inject

class Tracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    getAppLanguage: GetAppLanguage
) {

    init {
        val languageCode = getAppLanguage.call().code
        firebaseAnalytics.setUserProperty("app_language", languageCode)
    }

    fun trackScreenView(route: Route, screenClass: String) {
        val screenName = route.routeName.substringBefore("/")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    fun trackButtonClicked(button: TrackedButtons) {
        firebaseAnalytics.logEvent("button_clicked") {
            param("name", button.name.lowercase())
        }
    }

    fun trackStatusSelect() {
        firebaseAnalytics.logEvent("prayer_status_selected", null)
    }

    fun trackQuranChapterAdd() {
        firebaseAnalytics.logEvent("quran_chapter_add", null)
    }

    fun trackQuranChapterRemove() {
        firebaseAnalytics.logEvent("quran_chapter_remove", null)
    }

    fun trackLogin() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
    }

    fun trackLocationPermissionResult(granted: Boolean) {
        val suffix = if (granted) "granted" else "denied"
        firebaseAnalytics.logEvent("location_permission_${suffix}", null)
    }

    fun trackNotificationPermissionResult(granted: Boolean) {
        val suffix = if (granted) "granted" else "denied"
        firebaseAnalytics.logEvent("notification_permission_${suffix}", null)
    }

    enum class TrackedButtons {
        HOME_PRAYED_NOW,
        VIEW_NEXT_DAY_PRAYERS,
        VIEW_PREVIOUS_DAY_PRAYERS,
        STATUS_OVER_VIEW_BAR,
        ISHA_STATUSES_PERIOD_EXPLANATION,

        NEXT_QURAN_READING_SECTION,
        VIEW_FULL_QURAN_READING_SECTION,

        NOTIFICATION_PRAYED_NOW,

        ENABLE_STOP_MEDIA_ON_PRAYER_CALL,
        DISABLE_STOP_MEDIA_ON_PRAYER_CALL,

        NOTIFICATION_PERMISSION_SKIP,
        GOOGLE_SIGN_IN;
    }
}