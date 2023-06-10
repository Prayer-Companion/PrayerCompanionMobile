package com.prayercompanion.prayercompanionandroid.data.local.assets.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "quran")
class QuranDTO {
    @field:ElementList(inline = true, name = "sura")
    lateinit var chapters: List<QuranChapterDTO>
}
