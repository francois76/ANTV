package fr.fgognet.antv.view.cardList.playlist

import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.mapping.Bundle
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/LiveViewModel"

class NewPlaylistViewModel : AbstractCardListViewModel<PlaylistCardData>() {

    override fun loadCardData(force: Boolean) {
        Napier.v("loadCardData", tag = TAG)
        _cards.value =
            CardListViewData(
                arrayListOf(
                    // the last uploads
                    PlaylistCardData(
                        "Dernières publications",
                        "Les dernières publications",
                        "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Dernières publications"
                            )
                            return b
                        }(),
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        "Questions au gouvernement",
                        "Toute les questions au gouvernement",
                        "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Questions au gouvernement"
                            )
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Questions au gouvernement"
                            )
                            return b
                        }(),
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        "Séance publique",
                        "Toutes les séances publiques ainsi que les questions au gouvernement non montées",
                        "https://videos.assemblee-nationale.fr/live/images/14000000.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Séance publique"
                            )
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Séance publique"
                            )
                            return b
                        }(),
                    ),
                    // commission du développement durable
                    PlaylistCardData(
                        "Commission au développement durable",
                        "Tous les replays de la commission au développement durable",
                        "https://videos.assemblee-nationale.fr/live/images/419865.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Commission"
                            )
                            b.putString(
                                EventSearchQueryParams.Commission.toString(),
                                "Développement durable (commission)"
                            )
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Commission du développement durable"
                            )
                            return b
                        }(),
                    ),
                    // commission des affaires économiques
                    PlaylistCardData(
                        "Commission des affaires économiques",
                        "Tous les replays de la commission des affaires économiques",
                        "https://videos.assemblee-nationale.fr/live/images/419610.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Commission"
                            )
                            b.putString(
                                EventSearchQueryParams.Commission.toString(),
                                "Affaires économiques (commission)"
                            )
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Commission des affaires économiques"
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
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Commission des affaires culturelles et éducation"
                            )
                            return b
                        }(),
                    ),
                    // commission des affaires européennes
                    PlaylistCardData(
                        "Commission des affaires européennes",
                        "Tous les replays de la commission des affaires européennes",
                        "https://videos.assemblee-nationale.fr/live/images/415287.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Commission"
                            )
                            b.putString(
                                EventSearchQueryParams.Commission.toString(),
                                "Affaires européennes (commission)"
                            )
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Commission des affaires européennes"
                            )
                            return b
                        }(),
                    ),
                    // commission des affaires étrangères
                    PlaylistCardData(
                        "Commission des affaires étrangères",
                        "Tous les replays de la commission des affaires étrangères",
                        "https://videos.assemblee-nationale.fr/live/images/59047.jpg",
                        fun(): Bundle {
                            val b = Bundle()
                            b.putString(
                                EventSearchQueryParams.TypeVideo.toString(),
                                "Commission"
                            )
                            b.putString(
                                EventSearchQueryParams.Commission.toString(),
                                "Affaires étrangères (commission)"
                            )
                            b.putString(
                                EventSearchQueryParams.Tag.toString(),
                                "Commission des affaires étrangères"
                            )
                            return b
                        }(),
                    )

                ),
                // app.resources.getString(R.string.playlist_description)
                "playlist"
            )
    }
}