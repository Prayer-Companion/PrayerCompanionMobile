package com.prayercompanion.prayercompanionandroid.domain.utils.tracking

import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route

interface Tracker {

    fun trackScreenView(route: Route, screenClass: String)
    fun trackButtonClicked(button: TrackedButtons)
    fun trackStatusSelect()
    fun trackQuranChapterAdd()
    fun trackQuranChapterRemove()
    fun trackLogin()
    fun trackLocationPermissionResult(granted: Boolean)
    fun trackNotificationPermissionResult(granted: Boolean)
}