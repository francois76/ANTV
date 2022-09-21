package fr.fgognet.antv.view.cardList.playlist

import fr.fgognet.antv.MR
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.utils.ResourceOrText
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/LiveViewModel"

class PlaylistViewModel : AbstractCardListViewModel<PlaylistCardData, Unit>() {

    override fun loadCardData(params: Unit) {
        Napier.v("loadCardData", tag = TAG)
        _cards.value =
            CardListViewData(
                arrayListOf(
                    // the last uploads
                    PlaylistCardData(
                        ResourceOrText("Dernières publications"),
                        "Les dernières publications",
                        "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Dernières publications"
                            return b
                        }()
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        ResourceOrText("Questions au gouvernement"),
                        "Toute les questions au gouvernement",
                        "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Questions au gouvernement"
                            b[EventSearchQueryParams.TypeVideo] = "Questions au gouvernement"
                            return b
                        }()
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        ResourceOrText("Séance publique"),
                        "Toutes les séances publiques ainsi que les questions au gouvernement non montées",
                        "https://videos.assemblee-nationale.fr/live/images/14000000.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Séance publique"
                            b[EventSearchQueryParams.TypeVideo] = "Séance publique"
                            return b
                        }()
                    ),
                    // commission des lois
                    PlaylistCardData(
                        ResourceOrText("Commission des lois"),
                        "Tous les replays de la commission des lois",
                        "https://videos.assemblee-nationale.fr/live/images/59051.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Commission des lois"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Lois (commission)"
                            return b
                        }()
                    ),
                    // commission des finances
                    PlaylistCardData(
                        ResourceOrText("Commission des finances"),
                        "Tous les replays de la commission des finances",
                        "https://videos.assemblee-nationale.fr/live/images/59048.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Commission des finances"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Finances (commission)"
                            return b
                        }()
                    ),
                    // commission du développement durable
                    PlaylistCardData(
                        ResourceOrText("Commission au développement durable"),
                        "Tous les replays de la commission au développement durable",
                        "https://videos.assemblee-nationale.fr/live/images/419865.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Commission du développement durable"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Développement durable (commission)"
                            return b
                        }()
                    ),
                    // commission des affaires économiques
                    PlaylistCardData(
                        ResourceOrText("Commission des affaires économiques"),
                        "Tous les replays de la commission des affaires économiques",
                        "https://videos.assemblee-nationale.fr/live/images/419610.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Commission des affaires économiques"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Affaires économiques (commission)"
                            return b
                        }()
                    ),
                    // commission des affaires culturelles
                    PlaylistCardData(
                        ResourceOrText("Commission des affaires culturelles et éducation"),
                        "Tous les replays de la commission des affaires culturelles et éducation",
                        "https://videos.assemblee-nationale.fr/live/images/419604.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] =
                                "Commission des affaires culturelles et éducation"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Affaires culturelles et éducation (commission)"
                            return b
                        }()
                    ),
                    // commission des affaires européennes
                    PlaylistCardData(
                        ResourceOrText("Commission des affaires européennes"),
                        "Tous les replays de la commission des affaires européennes",
                        "https://videos.assemblee-nationale.fr/live/images/415287.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Commission des affaires européennes"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Affaires européennes (commission)"
                            return b
                        }()
                    ),
                    // commission des affaires étrangères
                    PlaylistCardData(
                        ResourceOrText("Commission des affaires étrangères"),
                        "Tous les replays de la commission des affaires étrangères",
                        "https://videos.assemblee-nationale.fr/live/images/59047.jpg",
                        fun(): Map<EventSearchQueryParams, String> {
                            val b = hashMapOf<EventSearchQueryParams, String>()
                            b[EventSearchQueryParams.Tag] = "Commission des affaires étrangères"
                            b[EventSearchQueryParams.TypeVideo] = "Commission"
                            b[EventSearchQueryParams.Commission] =
                                "Affaires étrangères (commission)"
                            return b
                        }()
                    )
                ),
                ResourceOrText(stringResource = MR.strings.playlist_description)
            )
    }
}