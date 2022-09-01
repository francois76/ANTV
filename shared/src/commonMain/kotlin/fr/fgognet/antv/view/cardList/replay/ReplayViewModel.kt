package fr.fgognet.antv.view.cardList.replay

import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/ReplayViewModel"

class NewReplayViewModel : AbstractCardListViewModel<ReplayCardData>() {

    var searchQueryFields: HashMap<EventSearchQueryParams, String> =
        HashMap<EventSearchQueryParams, String>()

    override fun loadCardData(force: Boolean) {
        Napier.v("loadCardData", tag = TAG)
        if (super.cards.value.title != null && !force) {
            return
        }
        viewModelScope.launch {


            val eventSearches: List<EventSearch> = try {
                EventSearchRepository.findEventSearchByParams(
                    searchQueryFields
                )
            } catch (e: Exception) {
                Napier.e(e.toString(), tag = TAG)
                arrayListOf()
            }

            withContext(Dispatchers.Main) {
                Napier.i("dispatching regenerated view", tag = TAG)
                super._cards.value =
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