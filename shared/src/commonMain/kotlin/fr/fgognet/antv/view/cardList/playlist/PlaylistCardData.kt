package fr.fgognet.antv.view.cardList.playlist

import com.soywiz.korim.bitmap.Bitmap
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.view.card.CardData

data class PlaylistCardData(
    override var title: String,
    override var description: String,
    override var imageCode: String,
    var targetBundle: Map<EventSearchQueryParams, String>,
    override var isLoaded: Boolean,
    override var image: Bitmap?

) : CardData()