package fr.fgognet.antv.view.cardList.playlist

import android.app.Application
import android.os.Bundle
import android.util.Log
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData


private const val TAG = "ANTV/PlaylistViewModel"

class PlaylistViewModel(application: Application) :
    AbstractCardListViewModel<PlaylistCardData>(application) {
    private val app = application
    override fun loadCardData(params: Bundle?, force: Boolean) {
        Log.v(TAG, "loadCardData: $params")
        _cardListData.value =
            CardListViewData<PlaylistCardData>(
                generateCardData(),
                app.resources.getString(R.string.playlist_description)
            )
    }

    private fun generateCardData(): List<PlaylistCardData> {
        Log.v(TAG, "generateCardData")
        val result = arrayListOf(
            PlaylistCardData(
                "Questions au gouvernement",
                "",
                "toute les questions au gouvernement",
                "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                "",
                app.resources.getString(R.string.card_button_label_playlist),
                android.R.attr.colorPrimaryDark,
                fun(): Bundle {
                    val b = Bundle()
                    b.putString(
                        EventSearchQueryParams.TypeVideo.toString(),
                        "Questions%20au%20gouvernement"
                    )
                    return b
                }(),
                true
            )
        )
        return result
    }

}