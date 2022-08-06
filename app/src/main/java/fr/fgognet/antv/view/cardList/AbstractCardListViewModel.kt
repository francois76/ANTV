package fr.fgognet.antv.view.cardList

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*

private const val TAG = "ANTV/AbstractCardListViewModel"

data class CardListViewData(
    var cards: List<CardData>,
    var title: String
)

enum class CardStatus {
    DISABLED, // when the card is invalid, the card is not displayed
    REPLAY, // the card is a replay card
    LIVE, // the card is a current livestream
    SCHEDULED, // the card is a scheduled livestream
    PAST_LIVE // the card is a live that is over
}

enum class CardType {

    VIDEO, // the card go to a video or a live
    PLAYLIST, // the card go to a playlist
}

data class CardData(
    var title: String,
    var subtitle: String,
    var description: String,
    var imageCode: String,
    var url: String,
    var buttonLabel: String,
    var cardStatus: CardStatus,
    var cardType: CardType,
    var targetBundle: Bundle?,
)

abstract class AbstractCardListViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {
    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    protected val _cardListData = MutableLiveData<CardListViewData>()
    val cardListData: LiveData<CardListViewData> get() = _cardListData

    override fun onStart(owner: LifecycleOwner) {
        Log.v(TAG, "onStart")
        super.onStart(owner)
    }


    abstract fun loadCardData(params: Bundle?, force: Boolean)


}