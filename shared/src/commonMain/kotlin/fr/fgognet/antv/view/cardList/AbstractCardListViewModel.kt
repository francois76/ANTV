package fr.fgognet.antv.view.cardList

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.view.card.CardData

data class CardListViewData<T : CardData>(
    var cards: List<T>,
    var title: String?
)

abstract class AbstractCardListViewModel<T : CardData, U> : ViewModel() {
    val _cards: MutableLiveData<CardListViewData<T>> =
        MutableLiveData(CardListViewData(cards = arrayListOf(), title = null))
    val cards: LiveData<CardListViewData<T>> = _cards.readOnly()

    fun start(params: U) = apply { loadCardData(params) }

    abstract fun loadCardData(params: U)

}