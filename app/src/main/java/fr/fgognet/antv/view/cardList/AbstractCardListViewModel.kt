package fr.fgognet.antv.view.live

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*

private const val TAG = "ANTV/AbstractCardListViewModel"

data class CardListViewData(
    var cards: List<CardData>,
    var title: String
)

data class CardData(
    var title: String,
    var subtitle: String,
    var description: String,
    var imageCode: String,
    var url: String,
    var buttonLabel: String,
    var isEnabled: Boolean
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