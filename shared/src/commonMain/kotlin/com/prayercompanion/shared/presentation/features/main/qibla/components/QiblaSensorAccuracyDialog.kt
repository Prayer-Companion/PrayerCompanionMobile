package com.prayercompanion.shared.presentation.features.main.qibla.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.stringResource

@Composable
fun QiblaSensorAccuracyDialog(
    onDismissRequest: () -> Unit = {}
) = PrayerCompanionAndroidTheme {

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = RoundedCornerShape(6)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .padding(12.dp),

                ) {

// todo coil doesn't support KMP yet, so we will wait till they do to display the GIF

//                Image(
//                    modifier = Modifier
//                        .size(150.dp)
//                        .align(alignment = Alignment.CenterHorizontally),
//                    painter = asyncPainterResource(Res.images.eight_in_air, imageLoader),
//                    contentDescription = "8 Shape in air"
//                )
                Text(
                    stringResource(id = Res.strings.qibla_dialog_sensor_accuracy_title),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    stringResource(id = Res.strings.qibla_dialog_sensor_accuracy_text),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary
                )

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(alignment = Alignment.End),
                ) {
                    Text(
                        stringResource(id = Res.strings.ok),
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h3,
                    )
                }
            }
        }

    }

}