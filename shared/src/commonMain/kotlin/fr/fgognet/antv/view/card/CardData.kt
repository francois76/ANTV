package fr.fgognet.antv.view.card

import com.soywiz.korim.bitmap.Bitmap

abstract class CardData {
    abstract var title: String
    abstract var description: String
    abstract var imageCode: String
    abstract var image: Bitmap?
    abstract var isLoaded: Boolean
}