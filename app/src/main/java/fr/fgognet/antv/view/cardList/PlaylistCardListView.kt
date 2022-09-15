package fr.fgognet.antv.view.cardList

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
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
    goToCurrentPlaying: () -> Unit,
) {
    val state by model.cards.ld().observeAsState()
    PlaylistCardListViewState(
        state = state,
        goToVideos = goToVideos,
        goToCurrentPlaying = goToCurrentPlaying
    )
}

@Composable
fun PlaylistCardListViewState(
    state: CardListViewData<PlaylistCardData>?,
    goToVideos: (bundle: Map<EventSearchQueryParams, String>) -> Unit,
    goToCurrentPlaying: () -> Unit,
) {
    AbstractCardListView(
        title = state?.title ?: stringResource(resource = MR.strings.title_playlist),
        cardDatas = state!!.cards,
        currentPlayingImage = PlayerService.currentMediaData?.bitmap?.asImageBitmap(),
        goToCurrentPlaying = goToCurrentPlaying
    ) { cardData: PlaylistCardData ->
        CompositeCardView(
            GenericCardData(
                title = cardData.title,
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(resource = MR.strings.card_button_label_playlist),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.White,
                enableButton = true
            ),
            buttonClicked = {
                goToVideos(cardData.targetBundle)
            }
        )
    }
}