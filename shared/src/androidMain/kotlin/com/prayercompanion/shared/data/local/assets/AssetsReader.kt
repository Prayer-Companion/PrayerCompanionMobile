package com.prayercompanion.shared.data.local.assets

import android.content.Context

actual class AssetsReader constructor(private val context: Context) {
    actual fun readFile(fileName: String): String {
        val file = context.assets.open("quran_data.json")
        return file.bufferedReader().readText()
    }
}