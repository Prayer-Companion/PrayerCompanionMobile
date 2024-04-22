package com.prayercompanion.shared.data.local.assets

import com.prayercompanion.shared.data.local.assets.dto.quran.QuranDTO
import com.prayercompanion.shared.failure
import com.prayercompanion.shared.fromJson

class Assets constructor(
    private val assetsReader: AssetsReader
) {

    val quran: Result<QuranDTO> by lazy {
        try {
            val jsonQuran = assetsReader.readFile("quran_data.json")
            val quranDTO = fromJson<QuranDTO>(jsonQuran)
                ?: return@lazy Result.failure("Parsing Quran failed")

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