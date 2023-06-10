package com.prayercompanion.prayercompanionandroid.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.prayercompanion.prayercompanionandroid.atEndOfDay
import com.prayercompanion.prayercompanionandroid.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.prayercompanionandroid.domain.models.Prayer
import com.prayercompanion.prayercompanionandroid.domain.models.PrayerStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface PrayersInfoDao {

    @Insert
    fun insertAll(prayersInfo: List<PrayerInfoEntity>)

    @Query("SELECT * FROM PrayersInfo WHERE dateTime >= :startDateTime and dateTime <= :endDateTime")
    fun getPrayers(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<PrayerInfoEntity>

    @Query("SELECT * FROM PrayersInfo WHERE prayer = :prayer AND dateTime >= :startOfDay and dateTime <= :endOfDay")
    fun getPrayer(
        prayer: Prayer,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): PrayerInfoEntity?

    @Query("SELECT status FROM PrayersInfo WHERE dateTime >= :startDateTime and dateTime <= :endDateTime AND prayer != :excludedPrayer")
    fun getPrayersStatusesByDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        excludedPrayer: Prayer = Prayer.DUHA
    ): Flow<List<PrayerStatus?>>

    @Query("UPDATE PrayersInfo SET status = :status WHERE dateTime >= :startOfDay and dateTime <= :endOfDay AND prayer = :prayer")
    fun updatePrayerStatus(
        prayer: Prayer,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime,
        status: PrayerStatus
    )

    @Query("Delete FROM PrayersInfo where dateTime >= :startDateTime and dateTime <= :endDateTime")
    fun delete(startDateTime: LocalDateTime, endDateTime: LocalDateTime)


    @Transaction
    fun deleteOldAndInsertNewTransaction(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        prayersInfo: List<PrayerInfoEntity>
    ) {
        delete(startDateTime, endDateTime)
        insertAll(prayersInfo)
    }

    fun getPrayer(prayer: Prayer, date: LocalDate): PrayerInfoEntity? {
        return getPrayer(
            prayer = prayer,
            startOfDay = date.atStartOfDay(),
            endOfDay = date.atEndOfDay()
        )
    }

    fun updatePrayerStatus(prayer: Prayer, date: LocalDate, status: PrayerStatus) {
        return updatePrayerStatus(
            prayer = prayer,
            startOfDay = date.atStartOfDay(),
            endOfDay = date.atEndOfDay(),
            status = status
        )
    }
}