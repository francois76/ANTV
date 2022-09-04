package fr.fgognet.antv.view.cardList.live

import com.soywiz.korim.bitmap.Bitmap
import fr.fgognet.antv.view.card.CardData

data class LiveCardData(
    override var title: String,
    var subtitle: String,
    override var description: String,
    override var imageCode: String,
    var url: String?,
    var buttonLabel: String,
    var isLive: Boolean,
    override var isLoaded: Boolean,
    override var image: Bitmap?

) : CardData()