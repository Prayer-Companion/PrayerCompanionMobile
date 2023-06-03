package com.prayercompanion.prayercompanionandroid.presentation.features.qibla.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme

@Preview
@Composable
fun QiblaSensorAccuracyDialog(
    onDismissRequest: () -> Unit = {}
) = PrayerCompanionAndroidTheme {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

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


                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    painter = rememberAsyncImagePainter(R.drawable.eight_in_air, imageLoader),
                    contentDescription = "8 Shape in air"
                )
                Text(
                    stringResource(id = R.string.qibla_dialog_sensor_accuracy_title),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    stringResource(id = R.string.qibla_dialog_sensor_accuracy_text),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary
                )

                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(alignment = Alignment.End),
                ) {
                    Text(
                        stringResource(id = R.string.ok),
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h3,
                    )
                }
            }
        }

    }

}