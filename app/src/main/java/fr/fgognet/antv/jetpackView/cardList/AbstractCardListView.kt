package fr.fgognet.antv.jetpackView.cardList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fr.fgognet.antv.R
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.card.CardData
import fr.fgognet.antv.view.card.CardViewModel
import fr.fgognet.antv.view.cardList.live.LiveCardData


@Composable
fun <T : CardData> AbstractCardListView(
    title: String,
    cardDatas: List<T>,
    currentPlayingImage: ImageBitmap?,
    cardDataGenerator: @Composable (T, CardViewModel) -> Unit
) {
    Column {
        Text(
            text = title, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        LazyRow(modifier = Modifier.weight(8f)) {
            items(cardDatas) { cardData ->
                cardDataGenerator(cardData, CardViewModel())
            }
        }
        if (PlayerService.currentMediaData != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    val (is_playing_thumbnail, is_playing_label, is_playing_title) = createRefs()

                    Text(
                        text = PlayerService.currentMediaData!!.title!!,
                        modifier = Modifier.constrainAs(is_playing_title) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(is_playing_thumbnail.end)
                            top.linkTo(is_playing_label.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        })
                    Image(
                        modifier = Modifier
                            .width(80.dp)
                            .constrainAs(is_playing_thumbnail) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                height = Dimension.fillToConstraints
                            },
                        bitmap = currentPlayingImage!!, contentDescription = ""
                    )
                    Text(
                        text = stringResource(id = R.string.is_playing),
                        modifier = Modifier
                            .height(20.dp)
                            .constrainAs(is_playing_label) {
                                end.linkTo(parent.end)
                                start.linkTo(is_playing_thumbnail.end)
                                top.linkTo(parent.top)
                                width = Dimension.fillToConstraints
                            })
                }
            }
        }
    }
}

@Preview(widthDp = 200, heightDp = 400)
@Composable
fun CardListViewPreview(
) {
    AbstractCardListView(
        title = "mytitle",
        cardDatas = arrayListOf(
            LiveCardData(
                "card1",
                "subtitle1",
                "loremipsum...",
                "",
                "",
                "Live",
                true
            )
        ),
        currentPlayingImage = null
    ) { _, _ ->

    }
}
