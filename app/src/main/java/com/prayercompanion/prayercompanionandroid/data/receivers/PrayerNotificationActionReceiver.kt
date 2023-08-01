package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.utils.notifications.PrayerNotificationAction
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerInfo
import com.prayercompanion.prayercompanionandroid.domain.usecases.prayers.SetPrayerStatusByDateTime
import com.prayercompanion.prayercompanionandroid.getSerializable
import com.prayercompanion.prayercompanionandroid.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class PrayerNotificationActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var setPrayerStatusByDateTime: SetPrayerStatusByDateTime

    @OptIn(ExperimentalStdlibApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        val prayerInfo =
            intent?.getSerializable(EXTRA_PRAYER_INFO, PrayerInfo::class.java) ?: return
        val actionType = intent.getSerializable(
            EXTRA_PRAYER_NOTIFICATION_ACTION,
            PrayerNotificationAction::class.java
        )

        if (actionType == PrayerNotificationAction.Prayed) {
            CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                val status = setPrayerStatusByDateTime.call(prayerInfo, LocalDateTime.now())

                withContext(Dispatchers.Main) {
                    context?.showToast(context.getString(status.nameId))
                }
            }
        }
    }

    companion object {
        const val EXTRA_PRAYER_INFO = "extra_prayer_info"
        const val EXTRA_PRAYER_NOTIFICATION_ACTION = "extra_prayer_notification_action"
    }
}