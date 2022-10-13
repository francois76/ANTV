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
import fr.fgognet.antv.view.cardList.replay.ReplayCardData
import fr.fgognet.antv.view.cardList.replay.ReplayViewModel
import fr.fgognet.antv.view.main.PlayingData


@Composable
fun ReplayCardListView(
    goToVideo: (title: String) -> Unit,
    playingData: PlayingData?,
    goToCurrentPlaying: () -> Unit
) {
    val model: ReplayViewModel = viewModel(
        factory = createViewModelFactory {
            ReplayViewModel().start(Unit)
        }
    )
    val state by model.cards.ld().observeAsState()
    ReplayCardListViewState(
        state = state,
        goToVideo = { url, imageCode, title, description ->
            model.insertVideoState(url, imageCode, title, description)
            goToVideo(title)
        },
        playingData = playingData,
        goToCurrentPlaying = goToCurrentPlaying,
        loadDestination = { model.loadNvs(it) })
}

@Composable
fun ReplayCardListViewState(
    state: CardListViewData<ReplayCardData>?,
    goToVideo: (url: String, imageCode: String, title: String, description: String) -> Unit,
    loadDestination: (code: String) -> Unit,
    playingData: PlayingData?,
    goToCurrentPlaying: () -> Unit,
) {
    val context = LocalContext.current
    AbstractCardListView(
        title = state?.title?.toString(context)
            ?: stringResource(resource = MR.strings.title_replay),
        cardDatas = state!!.cards,
        playingData = playingData,
        goToCurrentPlaying = goToCurrentPlaying
    ) { cardData: ReplayCardData ->
        if (cardData.nvsUrl == null) {
            loadDestination(cardData.nvsCode)
        }
        CompositeCardView(
            GenericCardData(
                title = cardData.title.toString(context),
                subTitle = cardData.subTitle,
                description = cardData.description,
                buttonName = stringResource(resource = MR.strings.card_button_label_replay),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.inversePrimary,
                buttonTextColor = Color.White,
                enableButton = cardData.buttonEnabled,
            ),
            buttonClicked = {
                goToVideo(
                    cardData.nvsUrl ?: "",
                    cardData.imageCode,
                    cardData.title.toString(context),
                    cardData.description
                )
            },
        )
    }
}