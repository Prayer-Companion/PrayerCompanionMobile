buildscript {
    val composeCompilerVersion by extra("1.5.1")
    val composeUiVersion by extra("1.4.3")
    val kotlinVersion by extra("1.9.0")
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion: String by project
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.6" apply false
}