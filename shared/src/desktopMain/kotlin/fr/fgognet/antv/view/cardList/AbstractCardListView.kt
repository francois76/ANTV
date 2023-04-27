package fr.fgognet.antv.view.cardList

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.utils.ResourceOrText
import fr.fgognet.antv.view.card.CompositeCardView
import fr.fgognet.antv.view.card.GenericCardData
import fr.fgognet.antv.view.cardList.playlist.PlaylistCardData
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModel

@Preview()
@Composable
fun CardListViewPreview(
) {
    AbstractCardListView(
        title = "mytitle",
        model = IsPlayingViewModel(),
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
    ) { cardData ->
        CompositeCardView(
            data = GenericCardData(
                title = cardData.title.getValue(),
                subTitle = null,
                description = cardData.description,
                buttonName = stringResource(MR.strings.card_button_label_playlist),
                imageCode = cardData.imageCode,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = Color.White,
                enableButton = true,
            ),
            buttonClicked = {},
        )
    }
}