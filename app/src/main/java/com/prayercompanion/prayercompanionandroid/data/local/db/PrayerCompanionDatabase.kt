package com.prayercompanion.prayercompanionandroid.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.MemorizedQuranChapterDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.PrayersInfoDao
import com.prayercompanion.prayercompanionandroid.data.local.db.daos.QuranReadingSectionsDao
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.MemorizedQuranChapterEntity
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.QuranReadingSectionEntity

@Database(
    entities = [
        PrayerInfoEntity::class,
        QuranReadingSectionEntity::class,
        MemorizedQuranChapterEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    version = 2
)
@TypeConverters(PrayerCompanionConverters::class)
abstract class PrayerCompanionDatabase : RoomDatabase() {
    abstract fun prayersInfoDao(): PrayersInfoDao
    abstract fun quranReadingSectionsDao(): QuranReadingSectionsDao
    abstract fun memorizedQuranChapterDao(): MemorizedQuranChapterDao
}