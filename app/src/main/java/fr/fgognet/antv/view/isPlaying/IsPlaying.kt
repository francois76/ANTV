package fr.fgognet.antv.view.isPlaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R

@Composable
fun IsPlaying(
    goToCurrentPlaying: () -> Unit,
    imageCode: String,
    title: String,
    description: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                goToCurrentPlaying()
            }
    ) {
        Row {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .width(80.dp),
                    model = imageCode,
                    placeholder = painterResource(R.drawable.ic_baseline_live_tv_24),
                    contentDescription = ""
                )
            }
            Column {
                Text(
                    text = stringResource(resource = MR.strings.is_playing),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    text = title,
                    modifier = Modifier
                )
                Text(
                    text = description,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(widthDp = 300, heightDp = 100)
@Composable
fun IsPlayingPreview() {
    IsPlaying(
        goToCurrentPlaying = {},
        title = "montitre", imageCode = "coucou", description = """
        Lorem ipsum dolor sit amet. Et molestiae illo non dolor At ipsa voluptas ex voluptas asperiores ad repudiandae enim eos veritatis eveniet. Aut voluptatum obcaecati At quis maxime ea aliquam consectetur sit error blanditiis.

        Quo repellendus laborum in atque vitae et tempore corporis ut consequatur consectetur quo debitis dignissimos. Cum dicta fugiat ut autem accusantium et ipsa modi. Ea corrupti quidem et magni voluptas est sunt delectus id deleniti dolores! Cum eveniet soluta et rerum repellat ut dolor magni in internos quia.

        Eos velit repellendus id saepe voluptatem eum tempore enim. Ea perspiciatis sapiente est voluptate nihil aut aliquid doloremque vel fugiat dignissimos qui laboriosam praesentium id culpa nemo sit distinctio. Quo autem consectetur vel nisi dolor aperiam sapiente.
        
    """
    )
}