package fr.fgognet.antv.view.cardList.replay

import fr.fgognet.antv.view.card.CardData

data class ReplayCardData(
    override var title: String,
    override var description: String,
    override var imageCode: String,
    var nvsCode: String,
    var nvsUrl: String?,
    var subTitle: String?

) : CardData()