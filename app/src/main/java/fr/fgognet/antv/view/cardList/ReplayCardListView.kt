package fr.fgognet.antv.view.cardList

import android.os.Bundle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.replay.ReplayCardData
import fr.fgognet.antv.view.cardList.replay.ReplayViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun convertBundleToMap(b: Bundle): HashMap<EventSearchQueryParams, String> {
    val result = hashMapOf<EventSearchQueryParams, String>()
    EventSearchQueryParams.allValues().forEach {
        if (b.containsKey(it.toString())) {
            result[it] = URLDecoder.decode(
                b.getString(it.toString()).toString(),
                StandardCharsets.UTF_8.toString()
            )
        }
    }
    return result
}

@Composable
fun ReplayCardListView(
    goToVideo: (title: String) -> Unit,
    goToCurrentPlaying: () -> Unit,
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
        goToVideo = { url, imageCode, title, description ->
            model.insertVideoState(url, imageCode, title, description)
            goToVideo(title)
        },
        goToCurrentPlaying = goToCurrentPlaying,
        loadDestination = { model.loadNvs(it) })
}

@Composable
fun ReplayCardListViewState(
    state: CardListViewData<ReplayCardData>?,
    goToVideo: (url: String, imageCode: String, title: String, description: String) -> Unit,
    loadDestination: (code: String) -> Unit,
    goToCurrentPlaying: () -> Unit,
) {
    val context = LocalContext.current
    AbstractCardListView(
        title = state?.title?.toString(context)
            ?: stringResource(resource = MR.strings.title_replay),
        cardDatas = state!!.cards,
        currentPlayingImage = PlayerService.currentMediaData?.bitmap?.asImageBitmap(),
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
                enableButton = true,
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