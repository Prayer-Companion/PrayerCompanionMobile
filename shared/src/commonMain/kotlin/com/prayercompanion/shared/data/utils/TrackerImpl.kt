package com.prayercompanion.shared.data.utils

import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker

expect class TrackerImpl: Tracker {

    override fun setAppLanguage(languageCode: String)
    override fun trackScreenView(screenName: String, screenClass: String)
    override fun trackButtonClicked(button: TrackedButtons)
    override fun trackStatusSelect()
    override fun trackQuranChapterAdd()
    override fun trackQuranChapterRemove()
    override fun trackLogin()
    override fun trackLocationPermissionResult(granted: Boolean)
    override fun trackNotificationPermissionResult(granted: Boolean)
}