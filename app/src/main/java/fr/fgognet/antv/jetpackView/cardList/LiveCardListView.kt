package fr.fgognet.antv.jetpackView.cardList

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import fr.fgognet.antv.R
import fr.fgognet.antv.jetpackView.card.CompositeCardView
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.cardList.live.LiveCardData

@Composable
fun LiveCardListView() {
    AbstractCardListView(
        title = stringResource(id = R.string.title_live),
        cardDatas = arrayListOf(),
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