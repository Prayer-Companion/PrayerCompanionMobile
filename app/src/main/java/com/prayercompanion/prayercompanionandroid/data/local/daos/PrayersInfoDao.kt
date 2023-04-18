package com.prayercompanion.prayercompanionandroid.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import java.time.LocalDate

@Dao
interface PrayersInfoDao {

    @Insert
    fun insertAll(prayersInfo: List<PrayerInfoEntity>)

    @Query("SELECT * FROM PrayersInfo WHERE date = :date")
    fun getPrayers(date: LocalDate): List<PrayerInfoEntity>

    @Query("SELECT * FROM PrayersInfo WHERE name = :prayer AND date = :date")
    fun getPrayer(prayer: Prayer, date: LocalDate): PrayerInfoEntity

    /**
     * exclusive
     * */
    @Query("DELETE FROM PrayersInfo WHERE date >= :startDate AND date < :endDate")
    fun delete(startDate: LocalDate, endDate: LocalDate)

}