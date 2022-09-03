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
import fr.fgognet.antv.utils.convertBundleToMap
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.replay.ReplayCardData
import fr.fgognet.antv.view.cardList.replay.ReplayViewModel

@Composable
fun ReplayCardListView(
    goToVideo: (url: String, imageCode: String) -> Unit,
    arguments: Bundle
) {
    val model: ReplayViewModel = viewModel(
        factory = createViewModelFactory {
            ReplayViewModel().start(convertBundleToMap(arguments))
        }
    )
    val state by model.cards.ld().observeAsState()
    ReplayCardListViewState(
        state = state,
        goToVideo = goToVideo,
        loadDestination = { model.loadNvs(it) })
}

@Composable
fun ReplayCardListViewState(
    state: CardListViewData<ReplayCardData>?,
    goToVideo: (url: String, imageCode: String) -> Unit,
    loadDestination: (code: String) -> Unit
) {
    AbstractCardListView(
        title = state?.title ?: stringResource(id = R.string.title_replay),
        cardDatas = state!!.cards,
        currentPlayingImage = PlayerService.currentMediaData?.bitmap?.asImageBitmap()
    ) { cardData: ReplayCardData, viewModel ->
        if (cardData.nvsUrl == null) {
            loadDestination(cardData.nvsCode)
        }
        CompositeCardView(
            GenericCardData(
                title = cardData.title,
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(id = R.string.card_button_label_replay),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.inversePrimary,
                buttonTextColor = Color.White,
                enableButton = true
            ),
            model = viewModel,
            buttonClicked = {
                goToVideo(cardData.nvsUrl ?: "", cardData.imageCode)
            }
        )
    }
}