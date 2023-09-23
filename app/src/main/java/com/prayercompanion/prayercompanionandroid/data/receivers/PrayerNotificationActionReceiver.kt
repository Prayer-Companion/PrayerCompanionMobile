package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.domain.extensions.now
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.SetPrayerStatusByDateTime
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.TrackedButtons
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.Tracker
import com.prayercompanion.prayercompanionandroid.getSerializable
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayerNotificationAction
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayersNotificationsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrayerNotificationActionReceiver : BroadcastReceiver(), KoinComponent {

    private val setPrayerStatusByDateTime: SetPrayerStatusByDateTime by inject()
    private val prayersNotificationsService: PrayersNotificationsService by inject()
    private val tracker: Tracker by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val prayerInfo = intent
            ?.getSerializable(EXTRA_PRAYER_INFO, PrayerInfo::class.java)
            ?: return

        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)

        val actionType = intent.getSerializable(
            EXTRA_PRAYER_NOTIFICATION_ACTION,
            PrayerNotificationAction::class.java
        )

        if (actionType == PrayerNotificationAction.Prayed) {
            tracker.trackButtonClicked(TrackedButtons.NOTIFICATION_PRAYED_NOW)
            CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                setPrayerStatusByDateTime.call(prayerInfo, LocalDateTime.now())
                    //Todo handle onFailure by showing some error message
                    .onSuccess { status ->
                        withContext(Dispatchers.Main) {
                            prayersNotificationsService.showNotificationSelectedStatus(
                                notificationId = notificationId,
                                prayerInfo = prayerInfo,
                                status = status
                            )
                        }
                    }
            }
        }
    }

    companion object {
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val EXTRA_PRAYER_INFO = "extra_prayer_info"
        const val EXTRA_PRAYER_NOTIFICATION_ACTION = "extra_prayer_notification_action"
    }
}