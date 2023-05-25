package com.prayercompanion.prayercompanionandroid.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.prayercompanion.prayercompanionandroid.data.local.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import java.time.LocalDate

@Dao
interface PrayersInfoDao {

    @Insert
    fun insertAll(prayersInfo: List<PrayerInfoEntity>)

    @Query("SELECT * FROM PrayersInfo WHERE date = :date")
    fun getPrayers(date: LocalDate): List<PrayerInfoEntity>

    @Query("SELECT * FROM PrayersInfo WHERE prayer = :prayer AND date = :date")
    fun getPrayer(prayer: Prayer, date: LocalDate): PrayerInfoEntity?

    @Query("UPDATE PrayersInfo SET status = :status WHERE date = :date AND prayer = :prayer")
    fun updatePrayerStatus(prayer: Prayer, date: LocalDate, status: PrayerStatus)

    /**
     * @param dates list of dates to delete prayers for
     * */
    @Query("Delete FROM PrayersInfo where date in (:dates)")
    fun delete(dates: List<LocalDate>)


    @Transaction
    fun deleteOldAndInsertNewTransaction(
        oldDates: List<LocalDate>,
        prayersInfo: List<PrayerInfoEntity>
    ) {
        delete(oldDates)
        insertAll(prayersInfo)
    }
}