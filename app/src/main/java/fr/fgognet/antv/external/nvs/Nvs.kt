package fr.fgognet.antv.external.nvs

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "data", strict = false)
class Nvs(
    @field:Element(name = "files")
    @param:Element(name = "files")
    var files: FilesList,

    @field:Element(name = "metadatas")
    @param:Element(name = "metadatas")
    var metadatas: MetadataList
) {
    fun getMeetingID(): String {

        return this.metadatas.metadatas
            .filter { it.name == "meeting_id" }
            .map { it.value }
            .filterNotNull()
            .first()
    }

    fun getReplayURL(): String {

        return this.files.files
            .filter { it.title == "source" }
            .map { it.url }
            .filterNotNull()
            .map { it.split("domain1")[1].split("_1.mp4")[0] }
            .map { "https://anorigin.vodalys.com/videos/definst/mp4/ida/domain1/$it.smil/master.m3u8" }
            .first()
    }

    fun getLiveURL(): String {

        return this.files.files
            .filter { it.title == "live" }
            .map { it.url?.split("?DVR")?.get(0) ?: "" }
            .first()
    }
}

@Root
data class FilesList(
    @field:ElementList(entry = "file", required = false, inline = true)
    @param:ElementList(entry = "file", required = false, inline = true)
    var files: List<File>
)


@Root(strict = false)
data class File(
    @field:Attribute(name = "title")
    @param:Attribute(name = "title")
    var title: String?,
    @field:Attribute(name = "url")
    @param:Attribute(name = "url")
    var url: String?
)

@Root
data class MetadataList(
    @field:ElementList(entry = "metadata", required = false, inline = true)
    @param:ElementList(entry = "metadata", required = false, inline = true)
    var metadatas: List<Metadata>
)

@Root(strict = false)
data class Metadata(
    @field:Attribute(name = "name")
    @param:Attribute(name = "name")
    var name: String?,
    @field:Attribute(name = "value")
    @param:Attribute(name = "value")
    var value: String?
)
