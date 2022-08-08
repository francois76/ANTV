package fr.fgognet.antv.view.cardList.replay

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.cardList.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "ANTV/ReplayViewModel"

class ReplayViewModel(application: Application) : AbstractCardListViewModel(application) {

    var searchQueryFields: HashMap<EventSearchQueryParams, String> = hashMapOf()

    override fun loadCardData(params: Bundle?, force: Boolean) {
        Log.v(TAG, "loadCardData: $params")
        if (super.cardListData.value?.title != null && !force) {
            return
        }
        val app = super.getApplication<Application>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val eventSearches: List<EventSearch>

                eventSearches = try {
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
                            generateCardData(eventSearches),
                            app.resources.getString(R.string.search_summary) + " " + searchQueryFields.keys.joinToString(
                                ", "
                            )
                        )
                }
            }
        }
    }

    private fun generateCardData(eventSearches: List<EventSearch>): List<CardData> {
        Log.v(TAG, "generateCardData")
        val result = arrayListOf<CardData>()
        viewModelScope.launch {
            for (eventSearch in eventSearches) {
                val cardData = CardData(
                    eventSearch.title ?: "video sans titre",
                    "",
                    eventSearch.description?.replace("<br>", "\n") ?: "",
                    if (eventSearch.thumbnail != null) eventSearch.thumbnail!!.replace(
                        "\\",
                        ""
                    ).replace(
                        "http",
                        "https"
                    ) else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                    "",
                    getApplication<Application>().resources.getString(R.string.card_button_label_replay),
                    CardStatus.DISABLED,
                    CardType.VIDEO,
                    null,
                )
                var urlReplay = ""
                var subTitle = ""
                var cardStatus = CardStatus.DISABLED
                withContext(Dispatchers.IO) {

                    if (eventSearch.url != null) {
                        val nvs = NvsRepository.getNvsByCode(
                            eventSearch.url!!
                        )
                        urlReplay = nvs.getReplayURL()
                        subTitle = nvs.getSubtitle()
                        cardStatus =
                            CardStatus.REPLAY // the status is valorized here to ensure the card actualy has the URL
                    }
                    withContext(Dispatchers.Main) {
                        cardData.url = urlReplay
                        cardData.cardStatus = cardStatus
                        cardData.subtitle = subTitle
                        result.add(cardData)
                    }
                }

            }

        }
        return result
    }
}