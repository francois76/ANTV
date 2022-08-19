package fr.fgognet.antv.view.cardList.playlist

import fr.fgognet.antv.view.card.CardData

data class PlaylistCardData(
    override var title: String,
    override var description: String,
    override var imageCode: String,
    var targetBundle: Any?,

    ) : CardData()