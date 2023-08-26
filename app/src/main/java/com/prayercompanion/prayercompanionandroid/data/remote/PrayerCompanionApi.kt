package com.prayercompanion.prayercompanionandroid.data.remote

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.MemorizedChapterVersesResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.QuranReadingSectionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import javax.inject.Inject

class PrayerCompanionApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getPrayers(
        timeZone: String,
        latitude: String,
        longitude: String,
        countryCode: String?,
        cityName: String?,
        monthOfYear: String
    ): List<DayPrayerResponse> {
        return client.get("v1/prayerTimes") {
            parameter("timeZone", timeZone)
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("countryCode", countryCode)
            parameter("cityName", cityName)
            parameter("monthOfYear", monthOfYear)
        }.body()
    }

    suspend fun signIn() {
        client.put("v1/user/signIn")
    }

    suspend fun updatePrayerStatus(
        prayerDate: String,
        prayerName: String,
        prayerStatus: String
    ) {
        client.put("v1/user/prayerStatus") {
            parameter("date", prayerDate)
            parameter("prayerName", prayerName)
            parameter("prayerStatus", prayerStatus)
        }
    }

    suspend fun getPrayerStatuses(
        startDate: String,
        endDate: String
    ): List<DayPrayerStatusResponse> {
        return client.get("v1/user/prayerStatuses") {
            parameter("startDate", startDate)
            parameter("endDate", endDate)
        }.body()
    }

    suspend fun getMemorizedChapterVerses(): List<MemorizedChapterVersesResponse> {
        return client.get("v1/user/memorizedSurahAyat").body()
    }

    suspend fun addOrUpdateMemorizedChapterVerses(
        chapterId: Int,
        startVerse: Int,
        endVerse: Int
    ) {
        client.put("v1/user/memorizedSurahAyat") {
            parameter("surahId", chapterId)
            parameter("startAya", startVerse)
            parameter("endAya", endVerse)
        }
    }

    suspend fun deleteMemorizedChapterVerses(chapterId: Int) {
        client.delete("v1/user/memorizedSurahAyat") {
            parameter("surahId", chapterId)
        }
    }

    suspend fun getQuranReadingSections(): List<QuranReadingSectionResponse> {
        return client.get("v1/user/quranReadingSections").body()
    }
}