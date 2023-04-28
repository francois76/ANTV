package fr.fgognet.antv.view.cardList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import fr.fgognet.antv.view.card.CardData
import fr.fgognet.antv.view.isPlaying.IsPlaying
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModel
import fr.fgognet.antv.widget.orientationWrapper


@Composable
fun <T : CardData> AbstractCardListView(
    title: String,
    cardDatas: List<T>,
    goToCurrentPlaying: () -> Unit,
    model: IsPlayingViewModel = getViewModel(
        factory = viewModelFactory {
            IsPlayingViewModel().start()
        }, key = "IsPlayingViewModel"
    ),
    cardDataGenerator: @Composable (T) -> Unit
) {
    orientationWrapper(
        portrait = {
            AbstractCardListViewPortrait(
                title = title,
                cardDatas = cardDatas,
                goToCurrentPlaying = goToCurrentPlaying,
                model = model,
                cardDataGenerator = cardDataGenerator
            )
        }, landscape = {
            AbstractCardListViewLandscape(
                title = title,
                cardDatas = cardDatas,
                goToCurrentPlaying = goToCurrentPlaying,
                model = model,
                cardDataGenerator = cardDataGenerator
            )
        }
    )
}

@Composable
fun <T : CardData> AbstractCardListViewPortrait(
    title: String,
    cardDatas: List<T>,
    goToCurrentPlaying: () -> Unit,
    model: IsPlayingViewModel,
    cardDataGenerator: @Composable (T) -> Unit
) {
    Column {
        Text(
            text = title, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        LazyRow(
            modifier = Modifier
                .weight(8f)
                .padding(bottom = 5.dp)
        ) {
            items(cardDatas) { cardData ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .width(300.dp)
                ) {
                    cardDataGenerator(cardData)
                }
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 5.dp)
        ) {
            IsPlaying(goToCurrentPlaying = goToCurrentPlaying, model = model)
        }
    }
}

@Composable
fun <T : CardData> AbstractCardListViewLandscape(
    title: String,
    cardDatas: List<T>,
    goToCurrentPlaying: () -> Unit,
    model: IsPlayingViewModel,
    cardDataGenerator: @Composable (T) -> Unit
) {
    Column {

        Row(modifier = Modifier.weight(2f)) {
            Column(
                modifier = Modifier.weight(2f), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            Column(
                modifier = Modifier
                    .weight(5f)
                    .padding(5.dp)
            ) {
                IsPlaying(goToCurrentPlaying = goToCurrentPlaying, model = model)
            }
        }
        LazyRow(
            modifier = Modifier.weight(8f)
        ) {
            items(cardDatas) { cardData ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .width(600.dp)
                ) {
                    cardDataGenerator(cardData)
                }
            }
        }

    }
}


