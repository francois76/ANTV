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
            CardListViewData(
                arrayListOf(
                    // the last uploads
                    PlaylistCardData(
                        "Dernières publications",
                        "les dernières publications",
                        "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        Bundle(),
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        "Questions au gouvernement",
                        "toute les questions au gouvernement",
                        "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Questions au gouvernement"
                            )
                            return b
                        }(),
                    ),
                    // commission des affaires culturelles
                    PlaylistCardData(
                        "Commission des affaires culturelles et éducation",
                        "Tous les replays de la commission des affaires culturelles et éducation",
                        "https://videos.assemblee-nationale.fr/live/images/419604.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Commission"
                            )
                            b.putString(
                                EventSearchQueryParams.Commission.toString(),
                                "Affaires culturelles et éducation (commission)"
                            )
                            return b
                        }(),
                    )
                ),
                app.resources.getString(R.string.playlist_description)
            )
    }


}