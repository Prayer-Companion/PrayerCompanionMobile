package com.prayercompanion.prayercompanionandroid.domain.utils.exceptions

object NoInternetConnectionException: Exception(
    "Couldn't connect to the internet, check your connection"
)

object LocationMissingException: Exception(
    "Can't retrieve device location"
)

object UnknownException: Exception(
    "Something went wrong"
)