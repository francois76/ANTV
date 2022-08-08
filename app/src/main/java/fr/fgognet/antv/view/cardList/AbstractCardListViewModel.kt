package fr.fgognet.antv.view.cardList

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/AbstractCardListViewModel"

data class CardListViewData(
    var cards: List<CardData>,
    var title: String
)


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
    var buttonBackgroundColorId: Int,
    var cardType: CardType,
    var targetBundle: Bundle?,
    var clickable: Boolean
) {
    var imageBitmap: Bitmap? = null
}

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

    fun loadRemoteCardData(cardData: CardData): CardData {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {


            }
        }
        return cardData
    }

}