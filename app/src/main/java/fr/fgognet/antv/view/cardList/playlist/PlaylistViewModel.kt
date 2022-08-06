package fr.fgognet.antv.view.cardList.playlist

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.image.ImageRepository
import fr.fgognet.antv.view.cardList.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "ANTV/PlaylistViewModel"

class PlaylistViewModel(application: Application) : AbstractCardListViewModel(application) {
    private val app = application
    override fun loadCardData(params: Bundle?, force: Boolean) {
        Log.v(TAG, "loadCardData: $params")
        _cardListData.value =
            CardListViewData(
                generateCardData(),
                app.resources.getString(R.string.playlist_description)
            )
    }

    private fun generateCardData(): List<CardData> {
        Log.v(TAG, "generateCardData")
        val result = arrayListOf(
            CardData(
                "Questions au gouvernement",
                "",
                "toute les questions au gouvernement",
                "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                "",
                app.resources.getString(R.string.card_button_label_playlist),
                CardStatus.REPLAY,
                CardType.PLAYLIST,
                fun(): Bundle {
                    val b = Bundle()
                    b.putString(
                        EventSearchQueryParams.TypeVideo.toString(),
                        "Questions%20au%20gouvernement"
                    )
                    return b
                }()
            )
        )
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