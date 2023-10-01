package com.prayercompanion.prayercompanionandroid.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayerNotificationAction
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayersNotificationsService
import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.domain.models.PrayerInfo
import com.prayercompanion.shared.domain.usecases.prayers.SetPrayerStatusByDateTime
import com.prayercompanion.shared.domain.utils.tracking.TrackedButtons
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import com.prayercompanion.shared.fromJson
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
            ?.getStringExtra(EXTRA_PRAYER_INFO)
            ?.let { fromJson<PrayerInfo>(it) }
            ?: return

        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)

        val actionType = intent.getStringExtra(EXTRA_PRAYER_NOTIFICATION_ACTION)
            ?.let { runCatching { PrayerNotificationAction.valueOf(it) }.getOrNull() }

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