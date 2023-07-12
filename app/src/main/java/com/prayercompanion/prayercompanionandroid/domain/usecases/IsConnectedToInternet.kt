package com.prayercompanion.prayercompanionandroid.domain.usecases

import javax.inject.Inject

class IsConnectedToInternet @Inject constructor() {

    /**
     * Pings Prayer-Companion api server to check for internet connection and server availability
     * @return `true` if both the device has **internet connection** and our server is **up and running**, and `false` otherwise
    * */
    fun call(): Boolean {
        kotlin.runCatching {
            val command = "ping -c 1 api.prayer-companion.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        }
        return false
    }
}