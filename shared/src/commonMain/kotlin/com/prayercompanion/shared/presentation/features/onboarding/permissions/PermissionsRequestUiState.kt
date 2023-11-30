package com.prayercompanion.shared.presentation.features.onboarding.permissions

import com.prayercompanion.shared.domain.utils.MokoPermissionsManager
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiText
import dev.icerock.moko.permissions.Permission

data class PermissionsRequestUiState(
    val icon: String = "ic_location.xml", //todo put all resource images in a wrapper class
    val title: UiText = UiText.StringResource(StringRes.location_permission_request_title),
    val body: UiText = UiText.StringResource(StringRes.location_permission_request_explanation),
    val ctaText: UiText = UiText.StringResource(StringRes.location_permission_request_cta),
    val permissions: List<Permission> = MokoPermissionsManager.locationPermissions,
    val skippable: Boolean = false
)