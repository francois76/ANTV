package fr.fgognet.antv.external.eventSearch

import kotlinx.serialization.Serializable

@Serializable
data class EventSearch(

    var nid: Int?,
    var mediaId: Int?,
    var date: String?,
    var thumbnail: String?,
    var published: Boolean,
    var vis: String?,
    var description: String?,
    var video_type: String?,
    var theme: String?,
    var commission: String?,
    var title: String?,
    var url: String?,
    var size: Int?

)

