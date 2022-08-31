package fr.fgognet.antv.jetpackView.cardList

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import fr.fgognet.antv.R
import fr.fgognet.antv.jetpackView.card.CompositeCardView
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.cardList.live.LiveCardData
import fr.fgognet.antv.view.cardList.live.NewLiveViewModel

@Composable
fun LiveCardListView(model: NewLiveViewModel) {
    AbstractCardListView(
        title = stringResource(id = R.string.title_live),
        cardDatas = model.cards.value.cards,
        cardDataGenerator = @Composable
        fun(
            cardData: LiveCardData, viewModel
        ) {
            CompositeCardView(
                title = cardData.title,
                subTitle = cardData.subtitle,
                description = cardData.description,
                buttonName = cardData.buttonLabel,
                model = viewModel
            )
        },
        currentPlayingImage = PlayerService.currentMediaData!!.bitmap?.asImageBitmap()
    )
}