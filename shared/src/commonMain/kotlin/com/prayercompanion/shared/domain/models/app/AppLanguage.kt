package com.prayercompanion.shared.domain.models.app

enum class AppLanguage(val code: String) {
    AR("ar"), EN("en");

    companion object {
        fun fromLanguageCode(code: String?): AppLanguage {
            return values().firstOrNull { it.code == code} ?: AR
        }
    }
}