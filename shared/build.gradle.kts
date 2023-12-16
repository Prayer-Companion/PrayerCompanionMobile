import com.prayercompanion.shared.gradle.ProjectConfig
import com.prayercompanion.shared.gradle.ProjectDependencies

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("native.cocoapods")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.google.firebase.crashlytics")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    cocoapods {
        version = "1.0"
        name = "Pods_PrayerCompanionIOS"
        summary = "PrayerCompanion CocoaPods"
        homepage = "https://github.com/JetBrains/kotlin"

        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp//Podfile")

        pod("FirebaseAnalytics")
        pod("GoogleSignIn") {
            version = "7.0.0"
        }

        framework {
            baseName = "shared"
            isStatic = true
            export("dev.icerock.moko:resources:0.23.0")
        }
    }

    //todo clean up dependencies
    sourceSets {
        val koinVersion = "3.5.0"
        val commonMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation(ProjectDependencies.ktorClientCore)
                implementation(ProjectDependencies.ktorClientLogging)
                implementation(ProjectDependencies.ktorClientContentNegotiation)
                implementation(ProjectDependencies.ktorClientSerialization)
                implementation("io.insert-koin:koin-core:$koinVersion")
                //compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                val voyagerVersion = "1.0.0-rc10"
                api("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                api("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
                api("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
                api("dev.gitlive:firebase-auth:1.10.4")
                api("dev.gitlive:firebase-crashlytics:1.10.4")

                api("dev.icerock.moko:permissions:0.16.0")
                api("dev.icerock.moko:permissions-compose:0.16.0")
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:resources-compose:0.23.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.mockk:mockk-common:1.12.5")
            }
        }
        val androidMain by getting {
            sourceSets {
                getByName("androidMain").kotlin.srcDirs("build/generated/moko/androidMain/src")
            }
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")

                implementation("app.cash.sqldelight:android-driver:2.0.0")
                implementation("androidx.datastore:datastore:1.0.0")

                implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
                implementation("com.google.firebase:firebase-analytics-ktx")
                implementation("com.google.firebase:firebase-crashlytics-ktx")
                implementation("com.google.android.gms:play-services-auth:20.7.0")
                implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
                implementation("com.google.android.gms:play-services-location:21.0.1")
                implementation(ProjectDependencies.ktorClientOkHttp)
                implementation(ProjectDependencies.ktorClientContentNegotiation)
                implementation(ProjectDependencies.ktorClientSerialization)
                implementation(ProjectDependencies.logcat)
                implementation("io.insert-koin:koin-android:$koinVersion")

                implementation(compose.preview)
                implementation(compose.uiTooling)

                implementation("com.google.android.play:review:2.0.1")
                implementation("com.google.android.play:review-ktx:2.0.1")
            }
        }
        val iosX64Main by getting {
            resources.srcDirs("build/generated/moko/iosX64Main/src")
        }
        val iosArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosArm64Main/src")
        }
        val iosSimulatorArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosSimulatorArm64Main/src")
        }
        val iosTest by creating {
            dependsOn(commonTest)
        }
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("app.cash.sqldelight:native-driver:2.0.0")
                implementation(ProjectDependencies.ktorClientDarwin)
            }
        }

    }
}

android {
    compileSdk = ProjectConfig.compileSdk
    namespace = "com.prayercompanion.shared"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        val prayerCompanionApiBaseUrl: String by project
        val ishaStatusesPeriodsExplanationUrl: String by project

        buildConfigField(
            "String",
            "PRAYER_COMPANION_API_BASE_URL",
            prayerCompanionApiBaseUrl
        )
        buildConfigField(
            "String",
            "ISHA_STATUSES_PERIODS_EXPLANATION_URL",
            ishaStatusesPeriodsExplanationUrl
        )
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

sqldelight {
    databases {
        create("PrayerCompanionDatabase") {
            packageName.set(ProjectConfig.appId)
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.prayercompanion.prayercompanionandroid.moko_resources"
    multiplatformResourcesClassName = "Res"
    multiplatformResourcesSourceSet = "commonMain"
}