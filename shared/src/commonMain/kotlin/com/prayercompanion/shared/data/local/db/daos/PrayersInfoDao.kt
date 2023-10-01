package com.prayercompanion.shared.data.local.db.daos

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.shared.data.local.db.PrayerCompanionConverters
import com.prayercompanion.shared.data.local.db.entities.PrayerInfoEntity
import com.prayercompanion.shared.domain.extensions.atEndOfDay
import com.prayercompanion.shared.domain.extensions.atStartOfDay
import com.prayercompanion.shared.domain.models.Prayer
import com.prayercompanion.shared.domain.models.PrayerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface PrayersInfoDao {
    fun insertAll(prayersInfo: List<PrayerInfoEntity>)
    fun getPrayers(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<PrayerInfoEntity>
    fun getPrayersFlow(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<PrayerInfoEntity>>

    fun getPrayer(
        prayer: Prayer,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): PrayerInfoEntity?

    fun getPrayersStatusesByDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        excludedPrayer: Prayer = Prayer.DUHA
    ): Flow<List<PrayerStatus?>>

    fun updatePrayerStatus(
        prayer: Prayer,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime,
        status: PrayerStatus
    )

    fun delete(startDateTime: LocalDateTime, endDateTime: LocalDateTime)
    fun deleteOldAndInsertNewTransaction(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        prayersInfo: List<PrayerInfoEntity>
    ) {
        delete(startDateTime, endDateTime)
        insertAll(prayersInfo)
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

class PrayersInfoDaoImpl constructor(
    db: PrayerCompanionDatabase
) : PrayersInfoDao {

    private val queries = db.prayersInfoQueries
    private val converters = PrayerCompanionConverters()

    override fun insertAll(prayersInfo: List<PrayerInfoEntity>) {
        queries.transaction {
            afterRollback { throw Exception("Failed to inserting prayers info ") }
            prayersInfo.forEach {
                queries.insert(
                    id = null,
                    prayer = it.prayer.name,
                    dateTime = converters.localDateToString(it.dateTime),
                    status = it.status.name
                )
            }
        }
    }

    override fun getPrayers(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): List<PrayerInfoEntity> {
        return getPrayersQuery(startDateTime, endDateTime).executeAsList()
    }

    override fun getPrayersFlow(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<PrayerInfoEntity>> {
        return getPrayersQuery(startDateTime, endDateTime)
            .asFlow().mapToList(Dispatchers.IO)
    }

    override fun getPrayer(
        prayer: Prayer,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): PrayerInfoEntity? {
        return queries.getPrayer(
            prayer = prayer.name,
            startOfDay = converters.localDateToString(startOfDay),
            endOfDay = converters.localDateToString(endOfDay)
        ) { id, prayerStr, dateTime, statusStr ->
            PrayerInfoEntity(
                id = id.toInt(),
                prayer = Prayer.valueOf(prayerStr),
                dateTime = converters.stringToLocalDate(dateTime),
                status = statusStr?.let { PrayerStatus.valueOf(it) } ?: PrayerStatus.None
            )
        }.executeAsOneOrNull()
    }

    override fun getPrayersStatusesByDate(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        excludedPrayer: Prayer
    ): Flow<List<PrayerStatus?>> {
        return queries.getPrayersStatusesByDate(
            startDateTime = converters.localDateToString(startDateTime),
            endDateTime = converters.localDateToString(endDateTime),
            excludedPrayer = excludedPrayer.name
        ) { status ->
            status?.let { PrayerStatus.valueOf(it) } ?: PrayerStatus.None
        }.asFlow().mapToList(Dispatchers.IO)
    }

    override fun updatePrayerStatus(
        prayer: Prayer,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime,
        status: PrayerStatus
    ) {
        queries.updatePrayerStatus(
            prayer = prayer.name,
            startOfDay = converters.localDateToString(startOfDay),
            endOfDay = converters.localDateToString(endOfDay),
            status = status.name
        )
    }

    override fun delete(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        queries.delete(
            startDateTime = converters.localDateToString(startDateTime),
            endDateTime = converters.localDateToString(endDateTime)
        )
    }

    private fun getPrayersQuery(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Query<PrayerInfoEntity> {
        return queries.getPrayers(
            startDateTime = converters.localDateToString(startDateTime),
            endDateTime = converters.localDateToString(endDateTime)
        ) { id, prayerStr, dateTime, statusStr ->
            PrayerInfoEntity(
                id = id.toInt(),
                prayer = Prayer.valueOf(prayerStr),
                dateTime = converters.stringToLocalDate(dateTime),
                status = statusStr?.let { PrayerStatus.valueOf(it) } ?: PrayerStatus.None
            )
        }
    }
}