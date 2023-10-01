package com.prayercompanion.prayercompanionandroid.presentation.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.R
import io.doorbell.android.Doorbell
import kotlinx.coroutines.delay

class FeedbackUtils constructor(
    private val activity: Activity,
) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val doorbell by lazy {
        Doorbell(
            activity,
            BuildConfig.DOORBELL_ID.toLong(),
            BuildConfig.DOORBELL_PRIVATE_KEY
        ).also {
            val email = firebaseAuth.currentUser?.email
            it.setEmail(email)
            it.addProperty("osVer", Build.VERSION.SDK_INT)
            it.emailField.isVisible = false

            it.dialogBuilder.setTitle(R.string.feedbackDialog_title)
            it.setPositiveButtonText(R.string.feedbackDialog_action_send)
            it.setNegativeButtonText(R.string.feedbackDialog_action_cancel)
            it.messageField.setHint(R.string.feedbackDialog_messageHint)
            it.messageField.minLines = 1
        }
    }

    suspend fun showFeedbackDialog() {
        doorbell.show()
        delay(200)
        doorbell.messageField.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(doorbell.messageField, 0)
    }
}