package fr.fgognet.antv.view.cardList.playlist

import fr.fgognet.antv.utils.ResourceOrText
import fr.fgognet.antv.view.card.CardData

data class PlaylistCardData(
    override var title: ResourceOrText,
    override var description: String,
    override var imageCode: String,
    var id: Int,

    ) : CardData()