package com.prayercompanion.shared.data.local.assets

expect class AssetsReader {
    fun readFile(fileName: String): String
}