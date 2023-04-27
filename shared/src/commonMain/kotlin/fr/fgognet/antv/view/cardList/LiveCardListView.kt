package fr.fgognet.antv.view.cardList

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.view.card.CompositeCardViewCard
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.live.LiveCardData
import fr.fgognet.antv.view.cardList.live.LiveViewModel

@Composable
fun LiveCardListView(
    model: LiveViewModel = viewModel(
        factory = createViewModelFactory {
            LiveViewModel().start(Unit)
        }
    ),
    updateContextualRefreshFunction: (() -> Unit) -> Unit,
    goToVideo: (title: String) -> Unit,
    goToCurrentPlaying: () -> Unit,
) {
    val state by model.cards.ld().observeAsState()
    updateContextualRefreshFunction {
        model.loadCardData(Unit)
    }
    LiveCardListViewState(
        state = state,
        goToVideo = { url, imageCode, title, description ->
            model.insertVideoState(url, imageCode, title, description)
            goToVideo(title)
        },
        goToCurrentPlaying = goToCurrentPlaying
    )
}

@Composable
fun LiveCardListViewState(
    state: CardListViewData<LiveCardData>?,
    goToVideo: (url: String, imageCode: String, title: String, description: String) -> Unit,
    goToCurrentPlaying: () -> Unit,
) {
    AbstractCardListView(
        title = state?.title?.getValue()
            ?: stringResource(resource = MR.strings.title_live),
        cardDatas = state!!.cards,
        goToCurrentPlaying = goToCurrentPlaying
    ) { cardData: LiveCardData ->
        val genericCardData: GenericCardData
        val title = cardData.title.getValue()
        if (cardData.isLive) {
            genericCardData = GenericCardData(
                title = cardData.title.getValue(),
                subTitle = cardData.subtitle,
                description = cardData.description,
                buttonName = cardData.buttonLabel.getValue(),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.error,
                buttonTextColor = Color.White,
                enableButton = true,
            )
        } else {
            genericCardData = GenericCardData(
                title = cardData.title.getValue(),
                subTitle = cardData.subtitle,
                description = cardData.description,
                buttonName = cardData.buttonLabel.getValue(),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.Black,
                enableButton = false,
            )
        }
        CompositeCardViewCard(
            genericCardData,
            buttonClicked = {
                goToVideo(
                    cardData.url ?: "",
                    cardData.imageCode,
                    title,
                    cardData.description,
                )
            },
        )

    }
}