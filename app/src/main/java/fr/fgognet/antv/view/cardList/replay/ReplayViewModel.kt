package fr.fgognet.antv.view.cardList.replay

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


private const val TAG = "ANTV/ReplayViewModel"

class ReplayViewModel(application: Application) :
    AbstractCardListViewModel<ReplayCardData>(application) {

    var searchQueryFields: EnumMap<EventSearchQueryParams, String> =
        EnumMap(EventSearchQueryParams::class.java)

    override fun loadCardData(params: Bundle?, force: Boolean) {
        Log.v(TAG, "loadCardData: $params")
        if (super.cardListData.value?.title != null && !force) {
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val eventSearches: List<EventSearch> = try {
                    EventSearchRepository.findEventSearchByParams(
                        searchQueryFields
                    )
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    arrayListOf()
                }

                withContext(Dispatchers.Main) {
                    Log.i(TAG, "dispatching regenerated view")
                    _cardListData.value =
                        CardListViewData(
                            eventSearches.map {
                                ReplayCardData(
                                    it.title ?: "video sans titre",
                                    it.description?.replace("<br>", "\n") ?: "",
                                    if (it.thumbnail != null) it.thumbnail!!.replace(
                                        "\\",
                                        ""
                                    ).replace(
                                        "http",
                                        "https"
                                    ) else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                                    it.url,
                                )
                            },
                            searchQueryFields[EventSearchQueryParams.Tag] ?: ""
                        )
                }
            }
        }
    }

}