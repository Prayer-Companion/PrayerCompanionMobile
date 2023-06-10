package com.prayercompanion.prayercompanionandroid.data.local.assets.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "aya")
class QuranVerseDTO {

    @field:Attribute(name = "bismillah", required = false)
    var bismillah: String = ""

    @field:Attribute(name = "index")
    var index: Int = 0

    @field:Attribute(name = "text")
    var text: String = ""
}