package com.prayercompanion.shared.domain.utils

import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

expect class PermissionsManager {
    val isLocationPermissionGranted: Boolean

    val isPushNotificationAllowed: Boolean

    companion object {
        val locationPermissions: List<String>
        val notificationsPermission: String
    }
}

class MokoPermissionsManager(
    permissionsControllerFactory: PermissionsControllerFactory
) {

    val permissionsController = permissionsControllerFactory.createPermissionsController()

    suspend fun isLocationPermissionGranted(): Boolean = coroutineScope {
        return@coroutineScope locationPermissions
            .map {
                async { permissionsController.isPermissionGranted(it) }
            }
            .awaitAll()
            .any { it }
    }

    suspend fun isPushNotificationAllowed(): Boolean {
        return permissionsController.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
    }

    companion object {
        val locationPermissions: List<Permission> = listOf(
            Permission.LOCATION,
            Permission.COARSE_LOCATION
        )
        val notificationsPermission: Permission = Permission.REMOTE_NOTIFICATION
    }
}