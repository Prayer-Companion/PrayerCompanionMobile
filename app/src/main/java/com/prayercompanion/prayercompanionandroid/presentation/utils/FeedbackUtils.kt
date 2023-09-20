package com.prayercompanion.prayercompanionandroid.presentation.utils

import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.R
import io.doorbell.android.Doorbell
import kotlinx.coroutines.delay


interface FeedbackUtils {
    suspend fun showFeedbackDialog()
}

class FeedbackUtilsImpl constructor(
    private val context: Context,
    private val doorbell: Doorbell
) : FeedbackUtils {

    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        val email = firebaseAuth.currentUser?.email
        doorbell.setEmail(email)
        doorbell.addProperty("osVer", Build.VERSION.SDK_INT)
        doorbell.emailField.isVisible = false

        doorbell.dialogBuilder.setTitle(R.string.feedbackDialog_title)
        doorbell.setPositiveButtonText(R.string.feedbackDialog_action_send)
        doorbell.setNegativeButtonText(R.string.feedbackDialog_action_cancel)
        doorbell.messageField.setHint(R.string.feedbackDialog_messageHint)
        doorbell.messageField.minLines = 1
    }

    override suspend fun showFeedbackDialog() {
        doorbell.show()
        delay(200)
        doorbell.messageField.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(doorbell.messageField, 0)
    }
}