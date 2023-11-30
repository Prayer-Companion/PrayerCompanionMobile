package com.prayercompanion.shared.domain.utils

import com.prayercompanion.shared.presentation.utils.log
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
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

    suspend fun requestPermission(permission: Permission): Boolean {
        return try {
            permissionsController.providePermission(permission)
            true
        } catch (deniedAlways: DeniedAlwaysException) {
            false
        } catch (denied: DeniedException) {
            false
        }.also {
            log { "Permission request: $permission = $it" }
        }
    }

    companion object {
        val locationPermissions: List<Permission> = listOf(
            Permission.LOCATION,
            Permission.COARSE_LOCATION
        )
        val notificationsPermission: Permission = Permission.REMOTE_NOTIFICATION
    }
}