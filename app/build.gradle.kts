plugins {
    id("kotlin-kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.firebase.crashlytics")
}
android {
    namespace = "com.prayercompanion.prayercompanionandroid"
    compileSdk = 33

    val composeCompilerVersion: String by project
    val composeUiVersion: String by project

    defaultConfig {
        applicationId = "com.prayercompanion.prayercompanionandroid"
        minSdk = 26
        targetSdk = 33
        versionCode = 5
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations += listOf("en", "ar")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }

        val prayerCompanionApiBaseUrl: String by project
        val ishaStatusesPeriodsExplanationUrl: String by project
        val doorbellId: String by project
        val doorbellPrivateKey: String by project

        buildConfigField("String", "PRAYER_COMPANION_API_BASE_URL", prayerCompanionApiBaseUrl)
        buildConfigField(
            "String",
            "ISHA_STATUSES_PERIODS_EXPLANATION_URL",
            ishaStatusesPeriodsExplanationUrl
        )
        buildConfigField("String", "DOORBELL_ID", doorbellId)
        buildConfigField("String", "DOORBELL_PRIVATE_KEY", doorbellPrivateKey)
    }
    signingConfigs {
        create("release") {
            storeFile = file("../prayer_companion_keystore.jks")
            keyAlias = "PrayerCompanionKeyStore"
            storePassword = "xiwzez-ruDwig-cidqy1"
            keyPassword = "xiwzez-ruDwig-cidqy1"
        }
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher_debug"
            manifestPlaceholders["app_icon_round"] = "@mipmap/ic_launcher_debug_round"

            signingConfig = android.signingConfigs.getByName("debug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            isDebuggable = false
            // TODO currently proguard is creating a lot of issues
            // lets revisit and make sure to set correct rules before enabling it again
            isMinifyEnabled = false
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher"
            manifestPlaceholders["app_icon_round"] = "@mipmap/ic_launcher_round"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeCompilerVersion: String by project
    val composeUiVersion: String by project

    val appcompatVersion = "1.7.0-alpha02"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.appcompat:appcompat-resources:$appcompatVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
    implementation("androidx.compose.material:material:$composeUiVersion")
    implementation("androidx.navigation:navigation-compose:2.7.0-beta01")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.github.skydoves:whatif:1.1.2")

    //  Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    val okHttpVersion = "5.0.0-alpha.11"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

    val roomVersion = "2.5.2"
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("com.squareup.logcat:logcat:0.1")
    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")

    //data store
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //GIF support
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-gif:2.4.0")

    val workManagerVersion = "2.8.1"
    implementation("androidx.work:work-runtime-ktx:$workManagerVersion")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("org.simpleframework:simple-xml:2.7.1")

    implementation("androidx.compose.material:material-icons-extended:$composeUiVersion")

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation("io.doorbell:android-sdk:0.4.7@aar")
//    ------------------------------------------------
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.room:room-testing:$roomVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")

    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("app.cash.turbine:turbine:0.7.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    testImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.20")
    androidTestImplementation("androidx.work:work-testing:$workManagerVersion")

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("app.cash.turbine:turbine:0.7.0")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    androidTestImplementation("io.mockk:mockk-android:1.10.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.47")
    androidTestImplementation("androidx.test:runner:1.5.2")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.47")
}