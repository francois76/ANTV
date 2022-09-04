package fr.fgognet.antv.view.cardList

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.external.image.ImageRepository
import fr.fgognet.antv.view.card.CardData
import kotlinx.coroutines.launch

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

    fun loadCard(title: String) {
        _cards.value =
            CardListViewData(
                title = cards.value.title,
                cards = cards.value.cards.map { cardData ->
                    if (cardData.title == title) {
                        viewModelScope.launch {
                            val image = ImageRepository.getLiveImage(cardData.imageCode)
                            cardData.image = image
                            cardData.isLoaded = true
                        }
                    }
                    cardData
                })
    }


}