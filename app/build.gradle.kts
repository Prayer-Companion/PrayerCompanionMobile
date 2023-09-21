import com.prayercompanion.shared.gradle.ProjectConfig

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.firebase.crashlytics")
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}

android {
    namespace = ProjectConfig.appId
    compileSdk = ProjectConfig.compileSdk

    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")

    val composeCompilerVersion: String by project
    val composeUiVersion: String by project

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations += listOf("en", "ar")

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
            signingConfig = android.signingConfigs.getByName("debug")

            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true

            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher_debug"
            manifestPlaceholders["app_icon_round"] = "@mipmap/ic_launcher_debug_round"

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            testProguardFiles("test-proguard-rules.pro")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher"
            manifestPlaceholders["app_icon_round"] = "@mipmap/ic_launcher_round"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            testProguardFiles("test-proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
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
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    val koinAndroidVersion = "3.5.0"
    implementation("io.insert-koin:koin-android:$koinAndroidVersion")

    val workManagerVersion = "2.8.1"
    implementation("androidx.work:work-runtime-ktx:$workManagerVersion")
    implementation("io.insert-koin:koin-androidx-workmanager:$koinAndroidVersion")

    val okHttpVersion = "5.0.0-alpha.11"
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

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

    implementation("androidx.compose.material:material-icons-extended:$composeUiVersion")

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation("io.doorbell:android-sdk:0.4.7@aar")

    val ktorVersion = "2.3.3"

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorVersion")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
//    ------------------------------------------------
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

    testImplementation("junit:junit:4.13.2")
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
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("app.cash.turbine:turbine:0.7.0")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    androidTestImplementation("io.mockk:mockk-android:1.10.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
}