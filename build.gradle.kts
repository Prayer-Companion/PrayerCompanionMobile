buildscript {
    val composeCompilerVersion by extra("1.5.1")
    val composeUiVersion by extra("1.4.3")
    val kotlinVersion by extra("1.9.0")
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
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
    val kotlinVersion = "1.9.0"
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.6" apply false
}