package fr.fgognet.antv.view.cardList.replay

import com.soywiz.korim.bitmap.Bitmap
import fr.fgognet.antv.view.card.CardData

data class ReplayCardData(
    override var title: String,
    override var description: String,
    override var imageCode: String,
    override var isLoaded: Boolean,
    override var image: Bitmap?,
    var nvsCode: String,
    var nvsUrl: String?,
    var subTitle: String?

) : CardData()