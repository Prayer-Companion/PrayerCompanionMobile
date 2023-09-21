package com.prayercompanion.shared

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase

class SQLDelightDriverFactory(private val context: Context) {
    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PrayerCompanionDatabase.Schema, context, "prayer-companion")
    }
}