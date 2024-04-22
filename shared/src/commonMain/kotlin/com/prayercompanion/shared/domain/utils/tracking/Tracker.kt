package com.prayercompanion.shared.domain.utils.tracking

interface Tracker {

    fun setAppLanguage(languageCode: String)
    fun trackScreenView(screenName: String, screenClass: String)
    fun trackButtonClicked(button: TrackedButtons)
    fun trackStatusSelect()
    fun trackQuranChapterAdd()
    fun trackQuranChapterRemove()
    fun trackLogin()
    fun trackLocationPermissionResult(granted: Boolean)
    fun trackNotificationPermissionResult(granted: Boolean)
}