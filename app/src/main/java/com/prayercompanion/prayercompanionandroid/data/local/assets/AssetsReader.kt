package com.prayercompanion.prayercompanionandroid.data.local.assets

import android.content.Context
import com.prayercompanion.prayercompanionandroid.data.local.assets.dto.QuranDTO
import dagger.hilt.android.qualifiers.ApplicationContext
import org.simpleframework.xml.core.Persister
import java.io.InputStream
import javax.inject.Inject

class AssetsReader @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    val quran: Result<QuranDTO> by lazy {
        val i = readAssetFile("quran_data.xml")
        i.use { inputStream ->
            try {
                val xmlString = inputStream.bufferedReader().use { it.readText() }
                val quranDTO = parseXml(QuranDTO::class.java, xmlString)
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

    private fun <T> parseXml(type: Class<T>, xmlString: String): T {
        val serializer = Persister()
        return serializer.read(type, xmlString)
    }

    private fun readAssetFile(fileName: String): InputStream {
        return context.assets.open(fileName)
    }
}