package fr.fgognet.antv.view.card

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.fgognet.antv.R

data class GenericCardData(
    var title: String,
    var subTitle: String?,
    var description: String,
    var buttonName: String,
    var imageCode: String,
    var buttonColor: Color,
    var buttonTextColor: Color,
    var enableButton: Boolean,
)

@Composable
fun CompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit,
) {
    ElevatedCard(colors = CardDefaults.cardColors()) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                placeholder = painterResource(R.drawable.ic_baseline_live_tv_24),
                model = data.imageCode,
                contentDescription = data.title
            )
            Column(modifier = Modifier
                .weight(8f)
                .padding(16.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = data.enableButton,
                    onClick = buttonClicked,
                    colors = ButtonDefaults.buttonColors(contentColor = data.buttonColor)
                ) {
                    Text(text = data.buttonName, color = data.buttonTextColor)
                }
                Text(
                    text = data.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (data.subTitle != null) {
                    Text(
                        text = data.subTitle!!,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    modifier = Modifier.scrollable(
                        ScrollableState { 0F },
                        enabled = true,
                        orientation = Orientation.Vertical,
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    text = data.description,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }


}

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