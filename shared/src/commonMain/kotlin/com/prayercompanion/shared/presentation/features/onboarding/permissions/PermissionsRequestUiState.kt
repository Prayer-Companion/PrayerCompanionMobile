package com.prayercompanion.shared.presentation.features.onboarding.permissions

import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.domain.utils.MokoPermissionsManager
import com.prayercompanion.shared.presentation.utils.UiText
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.resources.ImageResource

data class PermissionsRequestUiState(
    val icon: ImageResource = Res.images.ic_location,
    val title: UiText = UiText.StringResource(Res.strings.location_permission_request_title),
    val body: UiText = UiText.StringResource(Res.strings.location_permission_request_explanation),
    val ctaText: UiText = UiText.StringResource(Res.strings.location_permission_request_cta),
    val permissions: List<Permission> = MokoPermissionsManager.locationPermissions,
    val skippable: Boolean = false
)