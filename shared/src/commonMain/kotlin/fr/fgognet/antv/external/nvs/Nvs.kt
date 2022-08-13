package fr.fgognet.antv.external.nvs

import kotlinx.datetime.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("data")
class Nvs(
    @XmlElement(true)
    @SerialName("files")
    @XmlChildrenName("file")
    var files: List<File>,

    @XmlElement(true)
    @SerialName("metadatas")
    @XmlChildrenName("metadata")
    var metadatas: List<Metadata>
) {
    fun getMeetingID(): String {

        return this.metadatas
            .filter { it.name == "meeting_id" }.mapNotNull { it.value }
            .first()
    }

    fun getReplayURL(): String {

        return this.files
            .asSequence()
            .filter { it.title == "source" }
            .map { it.url }
            .filterNotNull()
            .map { it.split("domain1")[1].split("_1.mp4")[0] }
            .map { "https://anorigin.vodalys.com/videos/definst/mp4/ida/domain1/$it.smil/master.m3u8" }
            .first()
    }

    fun getContentType(): String {

        val metadatas = this.metadatas
            .filter { it.name == "commission" || it.name == "video_type" }
            .associateBy { it.name }
        return when (metadatas.size) {
            1 -> return metadatas["video_type"]?.label ?: ""
            2 -> return metadatas["commission"]?.label ?: metadatas["video_type"]?.label
            ?: ""
            else -> ""
        }
    }

    fun getTime(): LocalDateTime? {
        this.metadatas.first { it.name == "date" }.value?.toLong()?.let {

            return             Instant.fromEpochSeconds(
                it
            ).toLocalDateTime(TimeZone.currentSystemDefault())
        }
        return null
    }

}


@Serializable
data class File(
    @XmlElement(false)
    var title: String?,
    @XmlElement(false)
    var url: String?
)


@Serializable
data class Metadata(
    @XmlElement(false)
    var name: String?,
    @XmlElement(false)
    var value: String?,
    @XmlElement(false)
    var label: String?
)
