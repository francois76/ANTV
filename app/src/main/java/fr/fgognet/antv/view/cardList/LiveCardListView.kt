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
import fr.fgognet.antv.view.cardList.live.LiveCardData
import fr.fgognet.antv.view.cardList.live.NewLiveViewModel

@Composable
fun LiveCardListView(
    model: NewLiveViewModel = viewModel(
        factory = createViewModelFactory {
            NewLiveViewModel().start()
        }
    )
) {
    val state by model.cards.ld().observeAsState()
    LiveCardListViewState(state = state)
}

@Composable
fun LiveCardListViewState(state: CardListViewData<LiveCardData>?) {
    AbstractCardListView(
        title = state?.title ?: stringResource(id = R.string.title_live),
        cardDatas = state!!.cards,
        currentPlayingImage = PlayerService.currentMediaData?.bitmap?.asImageBitmap()
    ) { cardData: LiveCardData, viewModel ->
        CompositeCardView(
            title = cardData.title,
            subTitle = cardData.subtitle,
            description = cardData.description,
            buttonName = cardData.buttonLabel,
            model = viewModel
        )
    }
}