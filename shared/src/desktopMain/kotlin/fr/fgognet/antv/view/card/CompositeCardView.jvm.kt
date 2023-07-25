package fr.fgognet.antv.view.card

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Preview
@Composable
fun CompositeCardViewPreview() {
    CompositeCardView(
        GenericCardData(
            "title",
            "subtitle",
            """
        Lorem ipsum dolor sit amet. Et molestiae illo non dolor At ipsa voluptas ex voluptas asperiores ad repudiandae enim eos veritatis eveniet. Aut voluptatum obcaecati At quis maxime ea aliquam consectetur sit error blanditiis.

        Quo repellendus laborum in atque vitae et tempore corporis ut consequatur consectetur quo debitis dignissimos. Cum dicta fugiat ut autem accusantium et ipsa modi. Ea corrupti quidem et magni voluptas est sunt delectus id deleniti dolores! Cum eveniet soluta et rerum repellat ut dolor magni in internos quia.

        Eos velit repellendus id saepe voluptatem eum tempore enim. Ea perspiciatis sapiente est voluptate nihil aut aliquid doloremque vel fugiat dignissimos qui laboriosam praesentium id culpa nemo sit distinctio. Quo autem consectetur vel nisi dolor aperiam sapiente.
        
    """.trimIndent(),
            "live",
            "",
            buttonColor = MaterialTheme.colorScheme.secondary,
            buttonTextColor = Color.White,
            enableButton = true,
        ), buttonClicked = {}
    )
}