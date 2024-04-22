package com.prayercompanion.shared.domain.usecases

expect class IsConnectedToInternet {
    fun call(): Boolean
}