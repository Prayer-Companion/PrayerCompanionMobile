package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions

import androidx.annotation.DrawableRes
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.shared.domain.utils.PermissionsManager
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiText

data class PermissionsRequestUiState(
    @DrawableRes
    val icon: Int = R.drawable.ic_location,
    val title: UiText = UiText.StringResource(StringRes.location_permission_request_title),
    val body: UiText = UiText.StringResource(StringRes.location_permission_request_explanation),
    val ctaText: UiText = UiText.StringResource(StringRes.location_permission_request_cta),
    val permissions: List<String> = PermissionsManager.locationPermissions,
    val skippable: Boolean = false
)