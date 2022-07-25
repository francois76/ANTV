package fr.fgognet.antv.view.cardList.replay

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.external.Images.ImageRepository
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchManager
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
    override fun loadCardData(params: Bundle?) {
        Log.v(TAG, "loadCardData: $params")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var eventSearchs: List<EventSearch>
                var date: Long = params?.getLong("time")!!
                try {
                    eventSearchs =
                        EventSearchManager.findEventSearchByDate(
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
                true
            )
            result.add(cardData)
        }
        viewModelScope.launch {
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