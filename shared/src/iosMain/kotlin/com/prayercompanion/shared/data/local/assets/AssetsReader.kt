package com.prayercompanion.shared.data.local.assets

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

actual class AssetsReader {
    @OptIn(ExperimentalForeignApi::class)
    actual fun readFile(fileName: String): String {
        val path = NSBundle.mainBundle.pathForResource("quran_data", ofType = "json")
            ?: throw Exception("quran_dataxx.json File not found")
        return NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null)
            ?: throw Exception("failed to encode quran_dataxx.json file")
    }
}