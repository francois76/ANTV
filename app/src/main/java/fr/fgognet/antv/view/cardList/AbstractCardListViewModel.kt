package fr.fgognet.antv.view.cardList

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import fr.fgognet.antv.view.card.CardData

private const val TAG = "ANTV/AbstractCardListViewModel"

data class CardListViewData<T : CardData>(
    var cards: List<T>,
    var title: String
)


abstract class AbstractCardListViewModel<T : CardData>(application: Application) :
    AndroidViewModel(application),
    DefaultLifecycleObserver {
    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    protected val _cardListData = MutableLiveData<CardListViewData<T>>()
    val cardListData: LiveData<CardListViewData<T>> get() = _cardListData

    override fun onStart(owner: LifecycleOwner) {
        Log.v(TAG, "onStart")
        super.onStart(owner)
    }


    abstract fun loadCardData(params: Bundle?, force: Boolean)


}