package fr.fgognet.antv.view.card

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    val configuration = LocalConfiguration.current
    ElevatedCard(colors = CardDefaults.cardColors()) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeCompositeCardView(data = data, buttonClicked = buttonClicked)
            }
            else -> {
                PortraitCompositeCardView(data = data, buttonClicked = buttonClicked)
            }
        }
    }
}

@Composable
fun LandscapeCompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val (image_constraint, is_playing_label, is_playing_title) = createRefs()
        AsyncImage(
            modifier = Modifier
                .constrainAs(image_constraint) {
                    height = Dimension.fillToConstraints
                }
                .width(284.dp),
            placeholder = painterResource(R.drawable.ic_baseline_live_tv_24),
            model = data.imageCode,
            contentDescription = data.title
        )
    }
}

@Composable
fun PortraitCompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(),
            placeholder = painterResource(R.drawable.ic_baseline_live_tv_24),
            model = data.imageCode,
            contentDescription = data.title
        )
        Column(
            modifier = Modifier
                .weight(8f)
                .padding(16.dp)
        ) {
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
                modifier = Modifier.verticalScroll(
                    enabled = true,
                    state = ScrollState(0),
                ),
                color = MaterialTheme.colorScheme.secondary,
                text = data.description,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
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