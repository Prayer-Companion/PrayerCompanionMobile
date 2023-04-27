package com.prayercompanion.prayercompanionandroid.data.remote

import com.prayercompanion.prayercompanionandroid.data.remote.dto.DayPrayerResponse
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

    @PUT("v1/user/sign_in")
    suspend fun signIn()
}