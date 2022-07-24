package fr.fgognet.antv.external.nvs

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "data", strict = false)
data class Nvs(
    @field:ElementList(entry = "files", required = false, inline = false)
    @param:ElementList(entry = "files", required = false, inline = false)
    var files: List<File>
)

@Root(name = "files", strict = false)
data class File(
    @field:Attribute(name = "title")
    @param:Attribute(name = "title")
    var title: String?,
    @field:Attribute(name = "url")
    @param:Attribute(name = "url")
    var url: String?
)
