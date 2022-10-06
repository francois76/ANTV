package fr.fgognet.antv.view.cardList

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.utils.ResourceOrText
import fr.fgognet.antv.view.card.CardData
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.playlist.PlaylistCardData
import fr.fgognet.antv.view.isPlaying.IsPlaying

class HasMediaPlaying : ViewModel(), Player.Listener {
    private val tag = "ANTV/HasMediaPlaying"

    fun start() = apply { initialize() }

    private val _hasMediaPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasPlayingData: LiveData<Boolean> get() = _hasMediaPlaying


    private fun initialize() {
        PlayerService.controller?.addListener(this)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.v(tag, "onMediaItemTransition")
        Log.d(tag, mediaItem.toString())
        this._hasMediaPlaying.value = (mediaItem != null)
    }
}

@Composable
fun <T : CardData> AbstractCardListView(
    title: String,
    cardDatas: List<T>,
    goToCurrentPlaying: () -> Unit,
    cardDataGenerator: @Composable (T) -> Unit
) {
    val model: HasMediaPlaying = viewModel(factory = createViewModelFactory {
        HasMediaPlaying().start()
    }
    )
    val hasPlayingData by model.hasPlayingData.ld().observeAsState()
    AbstractCardListViewState(
        title = title,
        cardDatas = cardDatas,
        hasPlayingData = hasPlayingData,
        goToCurrentPlaying = goToCurrentPlaying,
        cardDataGenerator = cardDataGenerator
    )
}


@Composable
fun <T : CardData> AbstractCardListViewState(
    title: String,
    cardDatas: List<T>,
    hasPlayingData: Boolean?,
    goToCurrentPlaying: () -> Unit,
    cardDataGenerator: @Composable (T) -> Unit
) {

    val configuration = LocalConfiguration.current
    Column {
        Text(
            text = title, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        LazyRow(
            modifier = Modifier.weight(8f)
        ) {
            var modifier = Modifier.padding(horizontal = 2.dp)
            modifier = when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    modifier.width(600.dp)
                }
                else -> {
                    modifier.width(300.dp)
                }
            }
            items(cardDatas) { cardData ->
                Column(
                    modifier = modifier
                ) {
                    cardDataGenerator(cardData)
                }
            }
        }
        if (hasPlayingData == true) {
            Row(modifier = Modifier.weight(1f)) {
                IsPlaying(goToCurrentPlaying = goToCurrentPlaying)
            }

        }

    }
}

@Preview(widthDp = 200, heightDp = 400)
@Composable
@UnstableApi
fun CardListViewPreview(
) {
    AbstractCardListViewState(
        title = "mytitle",
        cardDatas = arrayListOf(
            PlaylistCardData(
                ResourceOrText("title1"),
                """
        Lorem ipsum dolor sit amet. Et molestiae illo non dolor At ipsa voluptas ex voluptas asperiores ad repudiandae enim eos veritatis eveniet. Aut voluptatum obcaecati At quis maxime ea aliquam consectetur sit error blanditiis.

        Quo repellendus laborum in atque vitae et tempore corporis ut consequatur consectetur quo debitis dignissimos. Cum dicta fugiat ut autem accusantium et ipsa modi. Ea corrupti quidem et magni voluptas est sunt delectus id deleniti dolores! Cum eveniet soluta et rerum repellat ut dolor magni in internos quia.

        Eos velit repellendus id saepe voluptatem eum tempore enim. Ea perspiciatis sapiente est voluptate nihil aut aliquid doloremque vel fugiat dignissimos qui laboriosam praesentium id culpa nemo sit distinctio. Quo autem consectetur vel nisi dolor aperiam sapiente.
        
    """,
                "imageCode1", 1
            ),
            PlaylistCardData(
                ResourceOrText("title2"), "description2", "imageCode1", 2
            )
        ),
        goToCurrentPlaying = {},
        hasPlayingData = true,
    ) { cardData ->
        CompositeCardView(
            data = GenericCardData(
                title = cardData.title.toString(LocalContext.current),
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(resource = MR.strings.card_button_label_playlist),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.White,
                enableButton = true,
            ),
            buttonClicked = {},
        )
    }
}
