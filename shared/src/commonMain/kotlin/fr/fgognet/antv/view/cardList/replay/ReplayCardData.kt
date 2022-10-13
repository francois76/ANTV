package fr.fgognet.antv.view.cardList.replay

import fr.fgognet.antv.utils.ResourceOrText
import fr.fgognet.antv.view.card.CardData

data class ReplayCardData(
    override var title: ResourceOrText,
    override var description: String,
    override var imageCode: String,
    var nvsCode: String,
    var nvsUrl: String?,
    var buttonEnabled: Boolean,
    var subTitle: String?

) : CardData()