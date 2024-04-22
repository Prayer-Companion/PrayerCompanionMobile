package com.prayercompanion.shared.presentation.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await

@Composable
actual fun InAppReview(
    onFailure: (Exception) -> Unit,
    onCompleted: () -> Unit
): InAppReviewAction {
    val context = LocalContext.current

    return object : InAppReviewAction {
        override suspend fun show() {
            val activity = context as? Activity ?: run {
                onFailure(IllegalStateException("Context is not an activity"))
                return
            }

            val manager = ReviewManagerFactory.create(context)
            val request = manager.requestReviewFlow()

            try {
                val response = request.await()
                try {
                    manager.launchReviewFlow(activity, response).await()
                    onCompleted()
                } catch (e: Exception) {
                    onFailure(e)
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}