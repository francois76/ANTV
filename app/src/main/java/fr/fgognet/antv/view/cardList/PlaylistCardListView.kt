package fr.fgognet.antv.view.cardList

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.playlist.PlaylistCardData
import fr.fgognet.antv.view.cardList.playlist.PlaylistViewModel

@Composable
fun PlaylistCardListView(
    model: PlaylistViewModel = viewModel(
        factory = createViewModelFactory {
            PlaylistViewModel().start(Unit)
        }
    ),
    goToVideos: (bundle: Map<EventSearchQueryParams, String>) -> Unit,
) {
    val state by model.cards.ld().observeAsState()
    PlaylistCardListViewState(state = state, goToVideos = goToVideos, loadCard = {
        model.loadCard(it)
    })
}

@Composable
fun PlaylistCardListViewState(
    state: CardListViewData<PlaylistCardData>?,
    loadCard: (title: String) -> Unit,
    goToVideos: (bundle: Map<EventSearchQueryParams, String>) -> Unit
) {
    AbstractCardListView(
        title = state?.title ?: stringResource(id = R.string.title_playlist),
        cardDatas = state!!.cards,
        currentPlayingImage = PlayerService.currentMediaData?.bitmap?.asImageBitmap()
    ) { cardData: PlaylistCardData ->
        CompositeCardView(
            GenericCardData(
                title = cardData.title,
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(id = R.string.card_button_label_playlist),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.White,
                enableButton = true,
                isLoaded = cardData.isLoaded,
                image = cardData.image
            ),
            loadCard = loadCard,
            buttonClicked = {
                goToVideos(cardData.targetBundle)
            }
        )
    }
}