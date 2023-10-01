# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Keeping Serializable data models
-keep class com.prayercompanion.shared.data.local.db.** { *; }
-keep class com.prayercompanion.shared.data.remote.dto.** { *; }
-keep class com.prayercompanion.shared.data.local.assets.dto.** { *; }
-keep class com.prayercompanion.shared.domain.models.** { *; }

-dontwarn org.slf4j.impl.StaticLoggerBinder

-dontwarn java.lang.ClassValue
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn org.junit.jupiter.api.extension.ExtensionContext
-dontwarn org.junit.jupiter.api.extension.ParameterContext
-dontwarn org.junit.jupiter.api.extension.ParameterResolver
-dontwarn org.junit.jupiter.api.extension.TestInstancePostProcessor