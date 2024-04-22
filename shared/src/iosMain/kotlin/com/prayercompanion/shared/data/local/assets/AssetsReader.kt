package com.prayercompanion.shared.data.local.assets

import com.prayercompanion.prayercompanionandroid.moko_resources.Res

actual class AssetsReader {
    actual fun readFile(fileName: String): String {
        return Res.files.quran_data.readText()
    }
}