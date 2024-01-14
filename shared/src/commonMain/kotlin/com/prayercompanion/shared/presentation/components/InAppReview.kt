package com.prayercompanion.shared.presentation.components

import androidx.compose.runtime.Composable

interface InAppReviewAction {
    suspend fun show()
}

@Composable
expect fun InAppReview(
    onFailure: (Exception) -> Unit,
    onCompleted: () -> Unit
): InAppReviewAction