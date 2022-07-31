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
            .filter { it.name == "meeting_id" }.mapNotNull { it.value }
            .first()
    }

    fun getReplayURL(): String {

        return this.files.files
            .asSequence()
            .filter { it.title == "source" }
            .map { it.url }
            .filterNotNull()
            .map { it.split("domain1")[1].split("_1.mp4")[0] }
            .map { "https://anorigin.vodalys.com/videos/definst/mp4/ida/domain1/$it.smil/master.m3u8" }
            .first()
    }

    fun getSubtitle(): String {

        val metadatas = this.metadatas.metadatas
            .filter { it.name == "commission" || it.name == "video_type" }
            .associateBy { it.name }
        return when (metadatas.size) {
            1 -> return metadatas["video_type"]?.label ?: ""
            2 -> return metadatas["commission"]?.label ?: metadatas["video_type"]?.label
            ?: ""
            else -> ""
        }
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
    @field:Attribute(name = "name", required = false)
    @param:Attribute(name = "name", required = false)
    var name: String?,
    @field:Attribute(name = "value", required = false)
    @param:Attribute(name = "value", required = false)
    var value: String?,
    @field:Attribute(name = "label", required = false)
    @param:Attribute(name = "label", required = false)
    var label: String?
)
