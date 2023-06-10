package com.prayercompanion.prayercompanionandroid.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity

@Database(entities = [PrayerInfoEntity::class], version = 1)
@TypeConverters(PrayerCompanionConverters::class)
abstract class PrayerCompanionDatabase: RoomDatabase() {
    abstract fun prayersInfoDao(): PrayersInfoDao
}