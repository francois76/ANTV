package fr.fgognet.antv.view.cardList

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.playlist.PlaylistCardData
import fr.fgognet.antv.view.cardList.playlist.PlaylistViewModel
import fr.fgognet.antv.view.main.PlayingData

@Composable
fun PlaylistCardListView(
    model: PlaylistViewModel = viewModel(
        factory = createViewModelFactory {
            PlaylistViewModel().start(Unit)
        }
    ),
    playingData: PlayingData?,
    goToVideos: () -> Unit,
    goToCurrentPlaying: () -> Unit,
) {
    val state by model.cards.ld().observeAsState()
    PlaylistCardListViewState(
        state = state,
        goToVideos = {
            model.setCurrentSearch(it)
            goToVideos()
        },
        playingData = playingData,
        goToCurrentPlaying = goToCurrentPlaying
    )
}

@Composable
fun PlaylistCardListViewState(
    state: CardListViewData<PlaylistCardData>?,
    goToVideos: (id: Int) -> Unit,
    playingData: PlayingData?,
    goToCurrentPlaying: () -> Unit,
) {
    AbstractCardListView(
        title = state?.title?.toString(LocalContext.current)
            ?: stringResource(resource = MR.strings.title_playlist),
        cardDatas = state!!.cards,
        playingData = playingData,
        goToCurrentPlaying = goToCurrentPlaying
    ) { cardData: PlaylistCardData ->
        CompositeCardView(
            GenericCardData(
                title = cardData.title.toString(LocalContext.current),
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(resource = MR.strings.card_button_label_playlist),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.White,
                enableButton = true
            ),
            buttonClicked = {
                goToVideos(cardData.id)
            }
        )
    }
}