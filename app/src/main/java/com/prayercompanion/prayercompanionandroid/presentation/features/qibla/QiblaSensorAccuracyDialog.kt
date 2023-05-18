package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.White

@Preview
@Composable
fun QiblaSensorAccuracyDialog(onDismissRequest: () -> Unit = {}) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = RoundedCornerShape(6)
        ) {
            Column(
                modifier = Modifier
                    .background(White)
                    .padding(12.dp),

                ) {


                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.eight_in_air),
                    contentDescription = "8 Shape in air"
                )
                Text(
                    stringResource(id = R.string.qibla_dialog_sensor_accuracy_title),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    stringResource(id = R.string.qibla_dialog_sensor_accuracy_text),
                    fontSize = 12.sp
                )

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(alignment = Alignment.End),
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            }
        }

    }

}