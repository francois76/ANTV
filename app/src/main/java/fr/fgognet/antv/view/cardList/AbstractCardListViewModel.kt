package fr.fgognet.antv.view.live

import android.app.Application
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
    var live: String,
    var buttonLabel: String,
    var isLive: Boolean
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
        loadCardData()
    }

    abstract fun loadCardData()


}