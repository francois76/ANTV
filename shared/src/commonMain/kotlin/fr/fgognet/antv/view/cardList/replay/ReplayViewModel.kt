package fr.fgognet.antv.view.cardList.replay

import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

private const val TAG = "ANTV/ReplayViewModel"

class NewReplayViewModel : AbstractCardListViewModel<ReplayCardData>() {

    var searchQueryFields: HashMap<EventSearchQueryParams, String> =
        HashMap<EventSearchQueryParams, String>()

    fun loadNvs(code: String) {
        _cards.value =
            CardListViewData(title = cards.value.title, cards = cards.value.cards.map { cardData ->
                if (cardData.nvsCode == code) {
                    viewModelScope.launch {
                        val nvs = NvsRepository.getNvsByCode(
                            cardData.nvsCode
                        )
                        cardData.nvsUrl = nvs.getReplayURL()

                        if (nvs.getTime() != null) {
                            val date = LocalDateTime.parse(nvs.getTime().toString())
                            cardData.subTitle =
                                "${date.dayOfMonth}/${date.monthNumber}/${date.year} ${date.hour}:${date.minute}"
                        }
                    }
                }
                cardData
            })
    }

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
                                it.url ?: "",
                                null, subTitle = null
                            )
                        },
                        searchQueryFields[EventSearchQueryParams.Tag] ?: ""
                    )
            }
        }
    }
}