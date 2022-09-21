package fr.fgognet.antv.view.card

import fr.fgognet.antv.utils.ResourceOrText

abstract class CardData {
    abstract var title: ResourceOrText
    abstract var description: String
    abstract var imageCode: String
}