buildscript {
    val composeCompilerVersion by extra("1.5.3")
    val composeUiVersion by extra("1.4.3")
    val kotlinVersion by extra("1.9.10")
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("dev.icerock.moko:resources-generator:0.23.0")
    }
    configurations.all {
        resolutionStrategy {
            dependencySubstitution {
                substitute(module("net.sf.proguard:proguard-gradle"))
                    .using(module("com.guardsquare:proguard-gradle:7.0.1"))
            }
        }
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.9.10"
    kotlin("multiplatform") version kotlinVersion apply false
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.compose") version ("1.5.2") apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.6" apply false
    id("app.cash.sqldelight") version ("2.0.0") apply (false)
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-18" apply false
    kotlin("native.cocoapods") version "1.9.10" apply false
}