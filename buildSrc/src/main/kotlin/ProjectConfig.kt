package com.prayercompanion.shared.gradle

object ProjectConfig {
    const val appId = "com.prayercompanion.prayercompanionandroid"
    const val compileSdk = 33
    const val minSdk = 26
    const val targetSdk = 33
    const val versionCode = 7
    const val versionName = "1.3.1"
}

object ProjectDependencies {
    const val ktorVersion = "2.3.3"
    const val ktorClientCore = "io.ktor:ktor-client-core:$ktorVersion"
    const val ktorClientOkHttp = "io.ktor:ktor-client-okhttp:$ktorVersion"
    const val ktorClientDarwin = "io.ktor:ktor-client-darwin:$ktorVersion"
    const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$ktorVersion"
    const val ktorClientSerialization = "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion"

    const val logcat = "com.squareup.logcat:logcat:0.1"
}