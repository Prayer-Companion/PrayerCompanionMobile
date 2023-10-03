package com.prayercompanion.shared.data.utils

import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons

actual class TrackerImpl {

    actual fun setAppLanguage(languageCode: String) {
        TODO()
    }

    actual fun trackScreenView(screenName: String, screenClass: String) {
        TODO()
    }

    actual fun trackButtonClicked(button: TrackedButtons) {
        TODO()
    }

    actual fun trackStatusSelect() {
        TODO()
    }

    actual fun trackQuranChapterAdd() {
        TODO()
    }

    actual fun trackQuranChapterRemove() {
        TODO()
    }

    actual fun trackLogin() {
        TODO()
    }

    actual fun trackLocationPermissionResult(granted: Boolean) {
        TODO()
    }

    actual fun trackNotificationPermissionResult(granted: Boolean) {
        TODO()
    }
}