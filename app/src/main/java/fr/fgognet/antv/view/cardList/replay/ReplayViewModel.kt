package fr.fgognet.antv.view.cardList.replay

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.external.Images.ImageRepository
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.live.AbstractCardListViewModel
import fr.fgognet.antv.view.live.CardData
import fr.fgognet.antv.view.live.CardListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


private const val TAG = "ANTV/ReplayViewModel"

class ReplayViewModel(application: Application) : AbstractCardListViewModel(application) {
    override fun loadCardData(params: Bundle?, force: Boolean) {
        Log.v(TAG, "loadCardData: $params")
        if (super.cardListData.value?.title != null && !force) {
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var eventSearchs: List<EventSearch>
                val date: Long = params?.getLong("time")!!
                try {
                    eventSearchs =
                        EventSearchRepository.findEventSearchByDate(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(date),
                                ZoneOffset.UTC
                            )
                        )
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                    eventSearchs = arrayListOf()
                }

                withContext(Dispatchers.Main) {
                    Log.i(TAG, "dispatching regenerated view")
                    _cardListData.value =
                        CardListViewData(generateCardData(eventSearchs), date.toString())
                }
            }
        }
    }

    private fun generateCardData(eventSearchs: List<EventSearch>): List<CardData> {
        Log.v(TAG, "generateCardData")
        val result = arrayListOf<CardData>()
        viewModelScope.launch {
            for (eventSearch in eventSearchs) {
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
                    "Voir",
                    false
                )
                var urlReplay = ""
                var isLive = false
                withContext(Dispatchers.IO) {

                    if (eventSearch.url != null) {
                        val nvs = NvsRepository.getNvsByCode(
                            eventSearch.url!!
                        )
                        urlReplay = nvs.getReplayURL()
                        isLive = true
                    }
                    withContext(Dispatchers.Main) {
                        cardData.url = urlReplay
                        cardData.isEnabled = isLive
                        result.add(cardData)
                    }
                }

            }

            withContext(Dispatchers.IO) {
                for (cardData in result) {
                    val bitmap = ImageRepository.getLiveImage(cardData.imageCode)
                    Log.w(TAG, "fetched bitmap :" + cardData.imageCode)
                    withContext(Dispatchers.Main) {
                        ImageRepository.imageCodeToBitmap[cardData.imageCode] = bitmap
                    }
                }
                withContext(Dispatchers.Main) {
                    _cardListData.value = _cardListData.value
                }
            }
        }
        return result
    }
}