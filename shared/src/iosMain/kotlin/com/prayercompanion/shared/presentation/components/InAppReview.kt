package com.prayercompanion.shared.presentation.components

import androidx.compose.runtime.Composable

@Composable
actual fun InAppReview(
    onFailure: (Exception) -> Unit,
    onCompleted: () -> Unit
): InAppReviewAction {
    return object : InAppReviewAction {
        override suspend fun show() {
            return
            TODO("ios")
        }
    }
}