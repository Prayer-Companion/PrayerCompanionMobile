package com.prayercompanion.prayercompanionandroid.data.remote

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerStatusResponse
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface PrayerCompanionApi {

    /**
     * @param timeZone: current location's time zone
     * @param latitude: current location's latitude
     * @param longitude: current location's longitude
     * @param monthOfYear: the month of year we want data for (MM/yyyy)
     * */
    @GET("v1/prayerTimes")
    suspend fun getPrayers(
        @Query("timeZone") timeZone: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("monthOfYear") monthOfYear: String,
    ): List<DayPrayerResponse>

    @PUT("v1/user/signIn")
    suspend fun signIn()

    /**
     * @param prayerDate: prayer date to updated (dd/MM/yyyy)
     * @param prayerName: prayer name
     * @param prayerStatus: new prayer status [Jamaah, OnTime, Late, Qadaa, Missed, NotSet]
     * */
    @PUT("v1/user/prayerStatus")
    suspend fun updatePrayerStatus(
        @Query("date") prayerDate: String,
        @Query("prayerName") prayerName: String,
        @Query("prayerStatus") prayerStatus: String
    )

    /**
     * @param startDate: starting date inclusive (dd/MM/yyyy)
     * @param endDate: ending date inclusive (dd/MM/yyyy)
     * */
    @GET("v1/user/prayerStatuses")
    suspend fun getPrayerStatuses(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<DayPrayerStatusResponse>


}