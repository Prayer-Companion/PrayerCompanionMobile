package com.prayercompanion.prayercompanionandroid.data.local.assets.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "sura")
class QuranChapterDTO {
    @field:ElementList(inline = true, name = "aya")
    lateinit var verses: List<QuranVerseDTO>

    @field:Attribute(name = "index")
    var index: Int = 0

    @field:Attribute(name = "name")
    var name: String = ""

}
