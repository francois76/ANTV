package fr.fgognet.antv.view.cardList.playlist

import fr.fgognet.antv.MR
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.repository.SearchDao
import fr.fgognet.antv.repository.SearchEntity
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
                        id = 1,
                        title = ResourceOrText("Dernières publications"),
                        description = "Les dernières publications",
                        imageCode = "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        id = 2,
                        title = ResourceOrText("Questions au gouvernement"),
                        description = "Toute les questions au gouvernement",
                        imageCode = "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                    ),
                    // questions au gouvernement
                    PlaylistCardData(
                        id = 3,
                        title = ResourceOrText("Séance publique"),
                        description = "Toutes les séances publiques ainsi que les questions au gouvernement non montées",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/14000000.jpg",
                    ),
                    // commission des lois
                    PlaylistCardData(
                        id = 4,
                        title = ResourceOrText("Commission des lois"),
                        description = "Tous les replays de la commission des lois",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/59051.jpg",
                    ),
                    // commission des finances
                    PlaylistCardData(
                        id = 5,
                        title = ResourceOrText("Commission des finances"),
                        description = "Tous les replays de la commission des finances",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/59048.jpg",
                    ),
                    // commission du développement durable
                    PlaylistCardData(
                        id = 6,
                        title = ResourceOrText("Commission au développement durable"),
                        description = "Tous les replays de la commission au développement durable",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/419865.jpg",
                    ),
                    // commission des affaires économiques
                    PlaylistCardData(
                        id = 7,
                        title = ResourceOrText("Commission des affaires économiques"),
                        description = "Tous les replays de la commission des affaires économiques",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/419610.jpg",
                    ),
                    // commission des affaires culturelles
                    PlaylistCardData(
                        id = 8,
                        title = ResourceOrText("Commission des affaires culturelles et éducation"),
                        description = "Tous les replays de la commission des affaires culturelles et éducation",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/419604.jpg",
                    ),
                    // commission des affaires européennes
                    PlaylistCardData(
                        id = 9,
                        title = ResourceOrText("Commission des affaires européennes"),
                        description = "Tous les replays de la commission des affaires européennes",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/415287.jpg",
                    ),
                    // commission des affaires étrangères
                    PlaylistCardData(
                        id = 10,
                        title = ResourceOrText("Commission des affaires étrangères"),
                        description = "Tous les replays de la commission des affaires étrangères",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/59047.jpg",
                    ),
                    // commission de la défense
                    PlaylistCardData(
                        id = 11,
                        title = ResourceOrText("Commission de la défense"),
                        description = "Tous les replays de la commission de la défense",
                        imageCode = "https://videos.assemblee-nationale.fr/live/images/59046.jpg",
                    )
                ),
                ResourceOrText(stringResource = MR.strings.playlist_description)
            )
    }

    fun setCurrentSearch(id: Int) {
        val queryParams = when (id) {

            1 -> hashMapOf()
            2 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Questions au gouvernement"
            )
            3 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Séance publique"
            )
            4 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Lois (commission)"
            )
            5 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Finances (commission)"
            )
            6 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Développement durable (commission)"
            )
            7 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Affaires économiques (commission)"
            )
            8 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Affaires culturelles et éducation (commission)"
            )
            9 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Affaires européennes (commission)"
            )
            10 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Affaires étrangères (commission)"
            )
            11 -> hashMapOf(
                EventSearchQueryParams.TypeVideo to "Commission",
                EventSearchQueryParams.Commission to "Défense (commission)"
            )
            else -> hashMapOf()
        }
        SearchDao.set(
            SearchEntity(
                label = cards.value.cards.find { it.id == id }?.title
                    ?: ResourceOrText("Dernières publications"), queryParams = queryParams
            )
        )
    }
}