package com.prayercompanion.shared.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(PrayerCompanionDatabase.Schema, "prayer-companion")
    }
}