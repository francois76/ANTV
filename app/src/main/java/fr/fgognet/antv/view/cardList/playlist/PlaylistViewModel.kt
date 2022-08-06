package fr.fgognet.antv.view.cardList.playlist

import android.app.Application
import android.os.Bundle
import android.util.Log
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardData
import fr.fgognet.antv.view.cardList.CardListViewData
import fr.fgognet.antv.view.cardList.CardStatus


private const val TAG = "ANTV/PlaylistViewModel"

class PlaylistViewModel(application: Application) : AbstractCardListViewModel(application) {
    private val app = application
    override fun loadCardData(params: Bundle?, force: Boolean) {
        Log.v(TAG, "loadCardData: $params")
        _cardListData.value =
            CardListViewData(
                arrayListOf(
                    CardData(
                        "Questions au gouvernement",
                        "",
                        "toute les questions au gouvernement",
                        "",
                        "",
                        app.resources.getString(R.string.card_button_label_playlist),
                        CardStatus.REPLAY
                    )
                ),
                app.resources.getString(R.string.playlist_description)
            )
    }

}