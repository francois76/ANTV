package fr.fgognet.antv.view.cardList

import android.os.Bundle
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
    ), goToVideos: (bundle: Bundle) -> Unit
) {
    val state by model.cards.ld().observeAsState()
    PlaylistCardListViewState(state = state, goToVideos = goToVideos)
}

@Composable
fun PlaylistCardListViewState(
    state: CardListViewData<PlaylistCardData>?,
    goToVideos: (bundle: Bundle) -> Unit
) {
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
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.White
            ),
            model = viewModel,
            buttonClicked = {
                goToVideos(cardData.targetBundle)
            }
        )
    }
}