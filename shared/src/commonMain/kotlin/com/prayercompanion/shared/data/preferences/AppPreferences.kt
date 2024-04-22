package com.prayercompanion.shared.data.preferences

import com.prayercompanion.shared.domain.models.Location
import com.prayercompanion.shared.domain.models.app.Address
import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val isSignedIn: Boolean,
    val location: Location?,
    val address: Address?,
    val hasSkippedNotificationPermission: Boolean,
    val deniedLocationPermissionsCount: Int,
    val deniedNotificationPermissionsCount: Int,
    /** The default app language should be arabic, so we set it to Arabic the first time only */
    val hasSetArabicLanguageFirstTime: Boolean,
    val isPauseMediaPreferencesEnabled: Boolean,
    val hasShownRateTheAppPopup: Boolean
)
