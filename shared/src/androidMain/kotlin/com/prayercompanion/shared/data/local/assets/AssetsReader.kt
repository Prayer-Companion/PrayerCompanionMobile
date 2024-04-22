package com.prayercompanion.shared.data.local.assets

import android.content.Context
import com.prayercompanion.prayercompanionandroid.moko_resources.Res

actual class AssetsReader constructor(private val context: Context) {
    actual fun readFile(fileName: String): String {
        return Res.files.quran_data.readText(context)
    }
}