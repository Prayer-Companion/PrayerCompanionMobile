package com.prayercompanion.shared.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PrayerCompanionDatabase.Schema, context, "prayer-companion")
    }
}