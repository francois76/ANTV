package fr.fgognet.antv.view.cardList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.fgognet.antv.R
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.playlist.NewPlaylistViewModel
import fr.fgognet.antv.view.cardList.playlist.PlaylistCardData

@Composable
fun PlaylistCardListView(
    model: NewPlaylistViewModel = viewModel(
        factory = createViewModelFactory {
            NewPlaylistViewModel().start()
        }
    )
) {
    val state by model.cards.ld().observeAsState()
    PlaylistCardListViewState(state = state)
}

@Composable
fun PlaylistCardListViewState(state: CardListViewData<PlaylistCardData>?) {
    AbstractCardListView(
        title = state?.title ?: stringResource(id = R.string.title_playlist),
        cardDatas = state!!.cards,
        currentPlayingImage = PlayerService.currentMediaData?.bitmap?.asImageBitmap()
    ) { cardData: PlaylistCardData, viewModel ->
        CompositeCardView(
            GenericCardData(
                title = cardData.title,
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(id = R.string.card_button_label_playlist),
                imageCode = cardData.imageCode
            ),
            model = viewModel
        )
    }
}