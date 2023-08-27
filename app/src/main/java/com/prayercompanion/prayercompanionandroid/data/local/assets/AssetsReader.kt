package com.prayercompanion.prayercompanionandroid.data.local.assets

import android.content.Context
import com.prayercompanion.prayercompanionandroid.data.local.assets.dto.quran.QuranDTO
import com.prayercompanion.prayercompanionandroid.fromJson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AssetsReader @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    val quran: Result<QuranDTO> by lazy {
        val i = context.assets.open("quran_data.json")
        i.use { inputStream ->
            try {
                val jsonQuran = inputStream.bufferedReader().use { it.readText() }
                val quranDTO = fromJson<QuranDTO>(jsonQuran)
                    ?: return@use Result.failure(Exception("Parsing Quran failed"))

                val chaptersWithoutFatiha = quranDTO.chapters.toMutableList().also {
                    it.removeFirst()
                }
                quranDTO.chapters = chaptersWithoutFatiha

                Result.success(quranDTO)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}