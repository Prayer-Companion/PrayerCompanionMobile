package com.prayercompanion.shared.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.prayercompanion.shared.presentation.utils.Strings
import com.prayercompanion.shared.presentation.utils.stringResource

@Composable
fun App() {
    MaterialTheme {
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                showImage = !showImage
            }) {
                Text(stringResource(Strings.app_name))
            }
            AnimatedVisibility(showImage) {
                Box(
                    Modifier
                        .size(80.dp)
                        .background(Color.Black)
                ) {

                }
            }
        }
    }
}